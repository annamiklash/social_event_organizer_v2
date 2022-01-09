package pjatk.socialeventorganizer.social_event_support.location.locationforevent.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.availability.location.model.LocationAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.location.service.LocationAvailabilityService;
import pjatk.socialeventorganizer.social_event_support.common.constants.Const;
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.repository.OrganizedEventRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.ActionNotAllowedException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotAvailableException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.mapper.LocationForEventMapper;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto.LocationForEventDto;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.repository.LocationForEventRepository;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.CANCELLED;
import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.CONFIRMED;

@Service
@AllArgsConstructor
@Slf4j
public class LocationForEventService {

    private final LocationForEventRepository locationForEventRepository;

    private final OrganizedEventRepository organizedEventRepository;

    private final CustomerRepository customerRepository;

    private final LocationService locationService;

    private final LocationAvailabilityService locationAvailabilityService;
    
    private final TimestampHelper timestampHelper;

    @Transactional(rollbackOn = Exception.class)
    public LocationForEvent create(long customerId, long eventId, long locationId, LocationForEventDto dto) {
        if (customerRepository.findById(customerId).isEmpty()) {
            throw new NotFoundException("Customer with id " + customerId + " DOES NOT EXIST");
        }
        final OrganizedEvent organizedEvent = organizedEventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event does not exist"));

        final String date = DateTimeUtil.fromLocalDateToDateString(organizedEvent.getDate());

        final boolean isAvailable = locationService.isAvailable(locationId, date, dto.getTimeFrom(), dto.getTimeTo());

        if (!isAvailable) {
            throw new NotAvailableException("Location not available for selected date and time");
        }
        final Location location = locationService.getWithAvailability(locationId, date);

        final String timeFrom = DateTimeUtil.joinDateAndTime(date, dto.getTimeFrom());
        final String timeTo = DateTimeUtil.joinDateAndTime(date, dto.getTimeTo());
        locationService.modifyAvailabilityAfterBooking(location, date, timeFrom, timeTo);

        final LocationForEvent locationForEvent = LocationForEventMapper.fromDto(dto);
        locationForEvent.setLocation(location);
        locationForEvent.setEvent(organizedEvent);
        organizedEvent.setLocationForEvent(locationForEvent);

        save(locationForEvent);

        return locationForEvent;
    }

    public LocationForEvent getWithLocation(long locationForEventId) {
        return locationForEventRepository.findByIdWithLocation(locationForEventId)
                .orElseThrow(() -> new NotFoundException("No location Reservation with id " + locationForEventId));
    }


    @Transactional(rollbackOn = Exception.class)
    public LocationForEvent cancelReservation(long locationForEventId) {
        final LocationForEvent locationForEvent = getWithLocationAndEvent(locationForEventId);

        if (!CollectionUtils.isEmpty(locationForEvent.getServices())) {
            throw new ActionNotAllowedException("Cannot cancel reservation for venue while there are service reservation for given event");
        }

        if (!CollectionUtils.isEmpty(locationForEvent.getCateringsForEventLocation())) {
            throw new ActionNotAllowedException("Cannot cancel reservation for venue while there are catering reservation for given event");
        }

        locationForEvent.setConfirmationStatus(CANCELLED.name());

        final OrganizedEvent event = locationForEvent.getEvent();

        final LocalTime timeFrom = locationForEvent.getTimeFrom();
        final LocalTime timeTo = locationForEvent.getTimeTo();
        final LocalDate date = event.getDate();

        final LocalDateTime dateTime = LocalDateTime.of(date, timeFrom);
        if (!isAllowedToCancel(dateTime)) {
            throw new ActionNotAllowedException("Cannot cancel reservation");
        }

        final String stringTimeFrom = DateTimeUtil.joinDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), DateTimeUtil.fromLocalTimeToString(timeFrom));
        final String stringTimeTo = DateTimeUtil.joinDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), DateTimeUtil.fromLocalTimeToString(timeTo));

        LocationAvailability locationAvailability = locationAvailabilityService.getByDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), stringTimeFrom, stringTimeTo);
        locationAvailabilityService.updateToAvailable(locationAvailability, locationForEvent.getLocation());

        event.setModifiedAt(timestampHelper.now());
        organizedEventRepository.save(event);

        return locationForEvent;
    }

    public LocationForEvent getWithLocationAndEvent(long locationForEventId) {
        return locationForEventRepository.getWithLocationAndEvent(locationForEventId)
                .orElseThrow(() -> new NotFoundException("No location Reservation with id " + locationForEventId));
    }

    public LocationForEvent confirmReservation(long locationId, long eventId) {
        final LocationForEvent locationForEvent = findByLocationIdAndEventId(locationId, eventId);

        locationForEvent.setConfirmationStatus(CONFIRMED.toString());
        save(locationForEvent);

        final OrganizedEvent organizedEvent = locationForEvent.getEvent();

        organizedEvent.setModifiedAt(timestampHelper.now());
        organizedEventRepository.save(organizedEvent);

        return locationForEvent;
    }

    public List<LocationForEvent> listAllByStatus(long locationId, String status) {
        return locationForEventRepository.findAllByLocationIdAndStatus(locationId, status);

    }

    public void save(LocationForEvent locationForEvent) {
        locationForEventRepository.save(locationForEvent);
    }

    public LocationForEvent findByLocationIdAndEventId(long id, long eventId) {
        return locationForEventRepository.findByEventIdAndLocationId(eventId, id)
                .orElseThrow(() -> new NotFoundException("Location for event does not exist"));
    }

    private boolean isAllowedToCancel(LocalDateTime dateTime) {
        return dateTime.minusDays(Const.MAX_CANCELLATION_DAYS_PRIOR).isAfter(timestampHelper.now());
    }


    public List<LocationForEvent> listAllByStatusAndBusinessId(long businessId, String status) {
        return locationForEventRepository.findAllBusinessIdAndStatus(businessId, status);
    }
}
