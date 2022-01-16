package pjatk.socialeventorganizer.social_event_support.event.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service.CateringForChosenEventLocationService;
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper;
import pjatk.socialeventorganizer.social_event_support.common.mapper.PageableMapper;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.enums.CustomerReservationTabEnum;
import pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum;
import pjatk.socialeventorganizer.social_event_support.event.helper.StatusChangeHelper;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.event.repository.OrganizedEventRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.service.LocationForEventService;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.service.OptionalServiceForLocationService;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrganizedEventService {

    private final OrganizedEventRepository organizedEventRepository;
    private final EventTypeService eventTypeService;
    private final CustomerRepository customerRepository;
    private final CateringForChosenEventLocationService cateringForChosenEventLocationService;
    private final LocationForEventService locationForEventService;
    private final OptionalServiceForLocationService optionalServiceForLocationService;
    private final StatusChangeHelper statusChangeHelper;
    private final TimestampHelper timestampHelper;

    public ImmutableList<OrganizedEventDto> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<OrganizedEvent> page = organizedEventRepository.findAll(paging);

        return page.get()
                .map(OrganizedEventMapper::toDtoWithCustomer)
                .collect(ImmutableList.toImmutableList());
    }

    public OrganizedEvent get(long orgEventId) {
        return organizedEventRepository.findById(orgEventId)
                .orElseThrow(() -> new NotFoundException("No organized event with id " + orgEventId));
    }

    public OrganizedEvent getWithDetail(long orgEventId, long customerId) {
        return organizedEventRepository.getWithDetail(orgEventId, customerId)
                .orElseThrow(() -> new NotFoundException("No organized event with id " + orgEventId));
    }

    public void save(OrganizedEvent organizedEvent) {
        organizedEventRepository.save(organizedEvent);
    }

    public OrganizedEvent getWithAllInformationForSendingInvitations(long eventId, long customerId) {
        return organizedEventRepository.getWithAllInformationForSendingInvitations(eventId, customerId)
                .orElseThrow(() -> new NotFoundException("No organized event with eventId " + eventId));
    }

    public OrganizedEvent changeStatus(long customerId, long eventId, EventStatusEnum status) {
        if (!eventWithIdAndCustomerIdExists(customerId, eventId)) {
            throw new NotFoundException("Event with id " + eventId + " and customer id " + customerId + " does not exist");
        }
        final OrganizedEvent organizedEvent = getWithDetail(eventId, customerId);

        switch (status) {
            case IN_PROGRESS: //possible only when current status CONFIRMED
                if (CONFIRMED.name().equals(organizedEvent.getEventStatus())) {
                    organizedEvent.setEventStatus(IN_PROGRESS.name());
                }
                break;
            case CONFIRMED: //if current IN_PROGRESS
                if (statusChangeHelper.possibleToChangeStatusFromInProgressToConfirmed(organizedEvent)) {
                    organizedEvent.setEventStatus(CONFIRMED.name());
                }
                break;
            case READY: //if current CONFIRMED
                if (CONFIRMED.name().equals(organizedEvent.getEventStatus())) {
                    organizedEvent.setEventStatus(READY.name());
                }
                break;
            case CANCELLED:  //any stage except for FINISHED
                cancel(organizedEvent);
                organizedEvent.setEventStatus(CANCELLED.name());
                break;
            case FINISHED:
                organizedEvent.setEventStatus(FINISHED.name());
                break;
        }

        save(organizedEvent);

        return organizedEvent;
    }

    public OrganizedEvent cancel(OrganizedEvent organizedEvent) {
        final LocationForEvent locationForEvent = organizedEvent.getLocationForEvent().stream()
                .filter(location -> !CANCELLED.name().equals(location.getConfirmationStatus()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No current reservation"));

        final Set<OptionalServiceForChosenLocation> services = locationForEvent.getServices();
        final Set<CateringForChosenEventLocation> caterings = locationForEvent.getCateringsForEventLocation();

        services.stream()
                .filter(Objects::nonNull)
                .forEach(optionalServiceForChosenLocation ->
                        optionalServiceForLocationService.cancelReservation(optionalServiceForChosenLocation.getId()));

        caterings.stream()
                .filter(Objects::nonNull)
                .forEach(catering -> cateringForChosenEventLocationService.cancelReservation(catering.getId()));

        locationForEventService.cancelReservation(locationForEvent.getId());

        organizedEvent.setEventStatus(CANCELLED.name());
        organizedEventRepository.save(organizedEvent);

        return organizedEvent;

    }

    public List<OrganizedEvent> getAllByCustomerIdAndTab(long customerId, CustomerReservationTabEnum tabEnum) {
        if (!customerRepository.existsById(customerId)) {
            throw new NotFoundException("No customer with " + customerId);
        }

        switch (tabEnum) {
            case ALL:
                return organizedEventRepository.findAllByCustomer_Id(customerId);
            case PAST:
                return organizedEventRepository.findAllFinished(customerId);
            case CURRENT:
                return organizedEventRepository.findAllCurrent(customerId);
            default:
                throw new IllegalArgumentException("Incorrect customer reservation type");
        }
    }

    public OrganizedEvent getWithLocation(long eventId) {
        return organizedEventRepository.getWithLocation(eventId)
                .orElseThrow(() -> new NotFoundException("No event with id " + eventId));
    }

    public OrganizedEvent create(long customerId, OrganizedEventDto dto) {
        final Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        final OrganizedEvent organizedEvent = OrganizedEventMapper.fromDto(dto);

        organizedEvent.setEventType(eventTypeService.getByType(dto.getEventType()));
        organizedEvent.setCustomer(customer);
        organizedEvent.setCreatedAt(timestampHelper.now());
        organizedEvent.setModifiedAt(timestampHelper.now());

        save(organizedEvent);

        return organizedEvent;
    }

    public void delete(OrganizedEvent organizedEvent) {
        organizedEvent.setModifiedAt(timestampHelper.now());
        organizedEvent.setDeletedAt(timestampHelper.now());

        save(organizedEvent);
    }

    private boolean eventWithIdAndCustomerIdExists(long customerId, long eventId) {
        return organizedEventRepository.existsOrganizedEventByIdAndCustomer_Id(eventId, customerId);
    }
}
