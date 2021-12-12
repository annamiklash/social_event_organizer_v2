package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.businesshours.DayEnum;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper.CateringForChosenLocationMapper;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.repository.CateringForLocationRepository;
import pjatk.socialeventorganizer.social_event_support.common.constants.Const;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService;
import pjatk.socialeventorganizer.social_event_support.exceptions.ActionNotAllowedException;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.LocationNotBookedException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.*;

@Service
@AllArgsConstructor
@Slf4j
public class CateringForChosenEventLocationService {

    private final CateringForLocationRepository cateringForLocationRepository;

    private final OrganizedEventService organizedEventService;

    private final CustomerService customerService;

    private final CateringService cateringService;

    public CateringForChosenEventLocation confirmReservation(long cateringId, long eventId) {
        final CateringForChosenEventLocation catering = findByCateringIdAndEventId(cateringId, eventId);

        catering.setConfirmationStatus(CONFIRMED.name());
        save(catering);

        final OrganizedEvent organizedEvent = catering.getEventLocation().getEvent();

        organizedEvent.setModifiedAt(LocalDateTime.now());
        organizedEventService.save(organizedEvent);

        return catering;
    }

    @Transactional(rollbackOn = Exception.class)
    public CateringForChosenEventLocation create(long customerId, long eventId, long cateringId, CateringForChosenEventLocationDto dto) {
        final Customer customer = customerService.get(customerId);
        final OrganizedEvent organizedEvent = organizedEventService.getWithLocation(eventId);
        if (organizedEvent.getLocationForEvent() == null) {
            throw new LocationNotBookedException("You cannot book catering prior to booking location");
        }
        final boolean isOpen = isOpen(cateringId, organizedEvent.getDate().getDayOfWeek().name());
        if (!isOpen) {
            throw new NotFoundException("No catering with id " + cateringId + " is not open on a given date");
        }

        final Catering catering = cateringService.get(cateringId);
        if (!organizedEvent.getLocationForEvent().getLocation().getCaterings().contains(catering)) {
            throw new NotFoundException("Catering cannot deliver to chosen location");
        }

        if (!dateValid(organizedEvent.getStartTime(), organizedEvent.getEndTime(), dto.getTime())) {
            throw new IllegalArgumentException("The time for catering booking should be between event time from and time to");
        }

        final CateringForChosenEventLocation cateringForLocation = CateringForChosenLocationMapper.fromDto(dto);

        cateringForLocation.setDate(organizedEvent.getDate());
        cateringForLocation.setEventLocation(organizedEvent.getLocationForEvent());
        cateringForLocation.setCatering(catering);

        final LocationForEvent locationForEvent = organizedEvent.getLocationForEvent();
        locationForEvent.setEvent(organizedEvent);

        save(cateringForLocation);

        return cateringForLocation;
    }

    @Transactional(rollbackOn = Exception.class)
    public CateringForChosenEventLocation cancelReservation(long cateringForEventId) {
        final CateringForChosenEventLocation cateringForLocation = getWithCateringAndEvent(cateringForEventId);
        final OrganizedEvent event = cateringForLocation.getEventLocation().getEvent();

        final LocalTime time = cateringForLocation.getTime();
        final LocalDate date = event.getDate();
        final LocalDateTime dateTime = LocalDateTime.of(date, time);

        if (!isAllowedToCancel(dateTime)) {
            throw new ActionNotAllowedException("Cannot cancel reservation");
        }
        cateringForLocation.setConfirmationStatus(CANCELLATION_PENDING.name());
        event.setModifiedAt(LocalDateTime.now());

        organizedEventService.save(event);
        save(cateringForLocation);

        return cateringForLocation;
    }

    @Transactional(rollbackOn = Exception.class)
    public CateringForChosenEventLocation setAsCancelled(long locationForEventId) {
        final CateringForChosenEventLocation cateringForChosenEventLocation = getWithCateringAndEvent(locationForEventId);
        if (!cateringForChosenEventLocation.getConfirmationStatus().equals(CANCELLATION_PENDING.name())) {
            throw new ActionNotAllowedException("Cannot confirm cancellation");
        }
        cateringForChosenEventLocation.setConfirmationStatus(CANCELLED.name());

        final OrganizedEvent event = cateringForChosenEventLocation.getEventLocation().getEvent();

        event.setModifiedAt(LocalDateTime.now());
        organizedEventService.save(event);

        return cateringForChosenEventLocation;
    }

    private boolean dateValid(LocalTime startTime, LocalTime endTime, String bookingTime) {
        return DateTimeUtil.toLocalTimeFromTimeString(bookingTime).isBefore(endTime)
                && DateTimeUtil.toLocalTimeFromTimeString(bookingTime).isAfter(startTime);
    }

    public List<CateringForChosenEventLocation> listAllByStatus(long cateringId, String status) {
        return cateringForLocationRepository.findAllByCateringIdAndStatus(cateringId, status);
    }

    private CateringForChosenEventLocation findByCateringIdAndEventId(long cateringId, long eventId) {
        return cateringForLocationRepository.findByCateringIdAndEventId(cateringId, eventId)
                .orElseThrow(() -> new NotFoundException("No catering for event " + eventId));
    }

    private void save(CateringForChosenEventLocation catering) {
        cateringForLocationRepository.save(catering);
    }

    private boolean isOpen(long cateringId, String day) {
        final Catering catering = cateringService.getWithBusinessHours(cateringId);
        return catering.getCateringBusinessHours().stream()
                .anyMatch(cateringBusinessHours -> cateringBusinessHours.getDay().equals(DayEnum.valueOfLabel(day).name()));
    }

    public CateringForChosenEventLocation getWithCateringAndEvent(long cateringForEventId) {
        return cateringForLocationRepository.getWithCateringAndEvent(cateringForEventId)
                .orElseThrow(() -> new NotFoundException("No location Reservation with id " + cateringForEventId));
    }

    public CateringForChosenEventLocation get(long cateringId) {
        return cateringForLocationRepository.findById(cateringId)
                .orElseThrow(() -> new NotFoundException("No booked catering with id " + cateringId + " was found"));
    }

    private boolean isAllowedToCancel(LocalDateTime dateTime) {
        return dateTime.minusDays(Const.MAX_CANCELLATION_DAYS_PRIOR).isAfter(LocalDateTime.now());
    }
}
