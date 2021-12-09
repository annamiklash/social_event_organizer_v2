package pjatk.socialeventorganizer.social_event_support.location.locationforevent.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService;
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
import java.util.Optional;

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.CONFIRMED;

@Service
@AllArgsConstructor
@Slf4j
public class LocationForEventService {

    private final LocationForEventRepository locationForEventRepository;

    private final OrganizedEventService organizedEventService;

    private final CustomerRepository customerRepository;

    private final LocationService locationService;

    public List<LocationForEvent> getLocationInfoByOrganizedEventId(long eventId) {
        final Optional<List<LocationForEvent>> optionalLocationForEvent = locationForEventRepository.findLocationForEventByOrganizedEventId(eventId);
        if (optionalLocationForEvent.isPresent()) {
            return optionalLocationForEvent.get();
        }
        throw new NotFoundException("Event with id " + eventId + " does not exist");
    }

    @Transactional(rollbackOn = Exception.class)
    public LocationForEvent create(long customerId, long eventId, long locationId, LocationForEventDto dto) {
        final Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("No customer with id " + customerId));
        final OrganizedEvent organizedEvent = organizedEventService.get(eventId);

        final String date = DateTimeUtil.toDateOnlyStringFromLocalDateTime(organizedEvent.getDate());
        final String timeFrom = DateTimeUtil.joinDateAndTime(date, dto.getTimeFrom());
        final String timeTo = DateTimeUtil.joinDateAndTime(date, dto.getTimeTo());

        final boolean isAvailable = locationService.isAvailable(locationId, date, timeFrom, timeTo);

        if (!isAvailable) {
            throw new NotAvailableException("Location not available for selected date and time");
        }
        final Location location = locationService.getWithAvailability(locationId, date);

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
                .orElseThrow(() -> new NotFoundException("No locationReservation with id " + locationForEventId));
    }

    public LocationForEvent cancelReservation(long locationForEventId) {
        final LocationForEvent locationForEvent = getWithLocation(locationForEventId);

        final LocalTime timeFrom = locationForEvent.getTimeFrom();
        final LocalTime timeTo = locationForEvent.getTimeTo();
        final LocalDate date = locationForEvent.getEvent().getDate();

        return null;

    }


    public LocationForEvent confirmReservation(long locationId, long eventId) {
        final LocationForEvent locationForEvent = findByLocationIdAndEventId(locationId, eventId);

        locationForEvent.setConfirmationStatus(CONFIRMED.toString());
        save(locationForEvent);

        final OrganizedEvent organizedEvent = locationForEvent.getEvent();

        organizedEvent.setModifiedAt(LocalDateTime.now());
        organizedEventService.save(organizedEvent);

        return locationForEvent;
    }

    public List<LocationForEvent> listAllByStatus(long locationId, String status) {
        return locationForEventRepository.findAllByLocationIdAndStatus(locationId, status);

    }

    public void save(LocationForEvent locationForEvent) {
        locationForEventRepository.save(locationForEvent);
    }

    public LocationForEvent findByLocationIdAndEventId(long id, long eventId) {

        final Optional<LocationForEvent> optionalLocationForEvent = locationForEventRepository.findByEventIdAndLocationId(eventId, id);

        if (optionalLocationForEvent.isPresent()) {
            return optionalLocationForEvent.get();
        }
        throw new NotFoundException("Location for event does not exist");
    }

}
