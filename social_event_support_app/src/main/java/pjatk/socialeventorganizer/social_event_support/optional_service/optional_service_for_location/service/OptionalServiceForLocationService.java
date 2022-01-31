package pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository.OptionalServiceAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.service.OptionalServiceAvailabilityService;
import pjatk.socialeventorganizer.social_event_support.common.constants.Const;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.repository.OrganizedEventRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.ActionNotAllowedException;
import pjatk.socialeventorganizer.social_event_support.exceptions.LocationNotBookedException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotAvailableException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.mapper.OptionalServiceForLocationMapper;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.dto.OptionalServiceForChosenLocationDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.repostory.OptionalServiceForChosenLocationRepository;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.availability.AvailabilityEnum.AVAILABLE;
import static pjatk.socialeventorganizer.social_event_support.availability.AvailabilityEnum.NOT_AVAILABLE;
import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.CANCELLED;
import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.CONFIRMED;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceForLocationService {

    private final OptionalServiceForChosenLocationRepository optionalServiceForChosenLocationRepository;

    private final CustomerRepository customerRepository;

    private final OrganizedEventRepository organizedEventRepository;

    private final OptionalServiceService optionalServiceService;

    private final OptionalServiceAvailabilityRepository optionalServiceAvailabilityRepository;

    private final OptionalServiceAvailabilityService optionalServiceAvailabilityService;

    @Transactional(rollbackOn = Exception.class)
    public OptionalServiceForChosenLocation create(long customerId, long eventId, long serviceId, OptionalServiceForChosenLocationDto dto) {
        if (customerRepository.findById(customerId).isEmpty()) {
            throw new NotFoundException("Customer with id " + customerId + " DOES NOT EXIST");
        }
        final OrganizedEvent organizedEvent = organizedEventRepository.getWithLocation(eventId)
                .orElseThrow(() -> new NotFoundException("Organized event does not exist"));

        if (organizedEvent.getLocationForEvent() == null) {
            throw new LocationNotBookedException("You cannot book service prior to booking services");
        }

        final String date = DateTimeUtil.fromLocalDateToDateString(organizedEvent.getDate());
        final boolean isAvailable = optionalServiceService.isAvailable(serviceId, date, dto.getTimeFrom(), dto.getTimeTo());

        if (!isAvailable) {
            throw new NotAvailableException("Service not available for selected date and time");
        }
        final OptionalService optionalService = optionalServiceService.get(serviceId);

        modifyAvailabilityAfterBooking(optionalService, date, dto.getTimeFrom(), dto.getTimeTo());

        final OptionalServiceForChosenLocation optionalServiceForChosenLocation = OptionalServiceForLocationMapper.fromDto(dto);
        optionalServiceForChosenLocation.setOptionalService(optionalService);
        final LocationForEvent locationForEvent = organizedEvent.getLocationForEvent()
                .stream()
                .filter(location -> !EventStatusEnum.CANCELLED.name().equals(location.getConfirmationStatus()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No current reservation"));

        locationForEvent.setEvent(organizedEvent);
        optionalServiceForChosenLocation.setLocationForEvent(locationForEvent);

        optionalServiceForChosenLocationRepository.save(optionalServiceForChosenLocation);
        return optionalServiceForChosenLocation;
    }

    public void modifyAvailabilityAfterBooking(OptionalService optionalService, String eventDate, String dateTimeFrom, String dateTimeTo) {

        final Set<OptionalServiceAvailability> serviceAvailability = optionalService.getAvailability();

        final LocalDate date = DateTimeUtil.fromStringToFormattedDate(eventDate);
        final LocalDateTime timeFrom = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(eventDate, dateTimeFrom));
        final LocalDateTime timeTo = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(eventDate, dateTimeTo));

        final List<OptionalServiceAvailability> availabilityForDate = serviceAvailability.stream()
                .filter(availability -> availability.getDate().equals(date))
                .filter(availability -> (timeFrom.isEqual(availability.getTimeFrom()) || timeFrom.isAfter(availability.getTimeFrom()))
                        && (timeTo.isEqual(availability.getTimeTo()) || timeTo.isBefore(availability.getTimeTo()))
                        && (availability.getStatus().equals(AVAILABLE.toString())))
                .collect(Collectors.toList());

        final OptionalServiceAvailability availability = availabilityForDate.get(0);

        final List<OptionalServiceAvailability> modified = modify(availability, date, timeFrom, timeTo);

        optionalServiceAvailabilityRepository.delete(availability);

        modified.forEach(optionalServiceAvailabilityRepository::saveAndFlush);

    }


    public OptionalServiceForChosenLocation confirmReservation(long serviceId, long eventId) {
        final OptionalServiceForChosenLocation optionalService = findByServiceIdAndEventId(serviceId, eventId);

        optionalService.setConfirmationStatus(CONFIRMED.name());
        optionalServiceForChosenLocationRepository.save(optionalService);

        final OrganizedEvent organizedEvent = optionalService.getLocationForEvent().getEvent();

        organizedEvent.setModifiedAt(LocalDateTime.now());
        organizedEventRepository.save(organizedEvent);

        return optionalService;
    }


    private List<OptionalServiceAvailability> modify(OptionalServiceAvailability availability, LocalDate bookingDate, LocalDateTime bookingTimeFrom, LocalDateTime bookingTimeTo) {

        final List<OptionalServiceAvailability> modified = new ArrayList<>();

        modified.add(OptionalServiceAvailability.builder()
                .status(NOT_AVAILABLE.toString())
                .date(bookingDate)
                .timeFrom(bookingTimeFrom)
                .timeTo(bookingTimeTo)
                .optionalService(availability.getOptionalService())
                .build());

        if (availability.getTimeFrom().isBefore(bookingTimeFrom)) {
            modified.add(OptionalServiceAvailability.builder()
                    .status(AVAILABLE.toString())
                    .date(bookingDate)
                    .timeFrom(availability.getTimeFrom())
                    .timeTo(bookingTimeFrom)
                    .optionalService(availability.getOptionalService())
                    .build());
        }

        if (availability.getTimeTo().isAfter(bookingTimeTo)) {
            modified.add(OptionalServiceAvailability.builder()
                    .status(AVAILABLE.toString())
                    .date(bookingDate)
                    .timeFrom(bookingTimeTo)
                    .timeTo(availability.getTimeTo())
                    .optionalService(availability.getOptionalService())
                    .build());
        }

        return modified;
    }

    private OptionalServiceForChosenLocation findByServiceIdAndEventId(long serviceId, long eventId) {
        return optionalServiceForChosenLocationRepository.findByServiceIdAndEventId(serviceId, eventId)
                .orElseThrow(() -> new NotFoundException("No optional service for event " + eventId));
    }

    public List<OptionalServiceForChosenLocation> listAllByStatus(long serviceId, String status) {
        return optionalServiceForChosenLocationRepository.findAllByServiceIdAndStatus(serviceId, status);
    }

    @Transactional(rollbackOn = Exception.class)
    public OptionalServiceForChosenLocation cancelReservation(long locationForEventId) {
        final OptionalServiceForChosenLocation serviceForEvent = getWithServiceAndEvent(locationForEventId);

        final OrganizedEvent event = serviceForEvent.getLocationForEvent().getEvent();

        final LocalTime timeFrom = serviceForEvent.getTimeFrom();
        final LocalTime timeTo = serviceForEvent.getTimeTo();
        final LocalDate date = event.getDate();
        final LocalDateTime dateTime = LocalDateTime.of(date, timeFrom);

        if (!isAllowedToCancel(dateTime)) {
            throw new ActionNotAllowedException("Cannot cancel reservation");
        }

        serviceForEvent.setConfirmationStatus(CANCELLED.name());

        final String stringTimeFrom = DateTimeUtil.joinDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), DateTimeUtil.fromLocalTimeToString(timeFrom));
        final String stringTimeTo = DateTimeUtil.joinDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), DateTimeUtil.fromLocalTimeToString(timeTo));

        OptionalServiceAvailability locationAvailability =
                optionalServiceAvailabilityService.getByDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), stringTimeFrom, stringTimeTo);
        optionalServiceAvailabilityService.updateToAvailable(locationAvailability, serviceForEvent.getOptionalService());

        event.setModifiedAt(LocalDateTime.now());

        optionalServiceForChosenLocationRepository.save(serviceForEvent);
        organizedEventRepository.save(event);

        return serviceForEvent;
    }

    private boolean isAllowedToCancel(LocalDateTime dateTime) {
        return dateTime.minusDays(Const.MAX_CANCELLATION_DAYS_PRIOR).isAfter(LocalDateTime.now());
    }

    public OptionalServiceForChosenLocation getWithServiceAndEvent(long locationForEventId) {
        return optionalServiceForChosenLocationRepository.getWithServiceAndEvent(locationForEventId)
                .orElseThrow(() -> new NotFoundException("No location Reservation with id " + locationForEventId));
    }


    public List<OptionalServiceForChosenLocation> listAllByStatusAndBusinessId(long businessId, String status) {
        return optionalServiceForChosenLocationRepository.findAllByBusinessIdAndStatus(businessId, status);
    }
}
