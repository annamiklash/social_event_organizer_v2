package pjatk.socialeventorganizer.social_event_support.event.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.enums.CustomerReservationTabEnum;
import pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.event.repository.OrganizedEventRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrganizedEventService {

    private final OrganizedEventRepository organizedEventRepository;

    private final EventTypeService eventTypeService;

    private final CustomerRepository customerRepository;

    public ImmutableList<OrganizedEventDto> list(CustomPage customPagination, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageRequest.of(customPagination.getFirstResult(), customPagination.getMaxResult(),
                Sort.by(customPagination.getSort()).descending());
        final Page<OrganizedEvent> page = organizedEventRepository.findAll(paging);

        return page.get().map(OrganizedEventMapper::toDtoWithCustomer).collect(ImmutableList.toImmutableList());
    }

    public OrganizedEvent get(long orgEventId) {
        final Optional<OrganizedEvent> optionalEvent = organizedEventRepository.findById(orgEventId);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        }
        throw new NotFoundException("No organized event with id " + orgEventId);
    }

    //TODO: controller method + mapper
    public OrganizedEvent getWithDetail(long orgEventId) {
        final Optional<OrganizedEvent> optionalEvent = organizedEventRepository.getWithDetail(orgEventId);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        }
        throw new NotFoundException("No organized event with id " + orgEventId);
    }

    public void save(OrganizedEvent organizedEvent) {
        organizedEventRepository.save(organizedEvent);
    }

    public OrganizedEvent getWithAllInformationForSendingInvitations(long eventId, long customerId) {
        final Optional<OrganizedEvent> organizedEvent = organizedEventRepository.getWithAllInformationForSendingInvitations(eventId, customerId);

        if (organizedEvent.isPresent()) {
            return organizedEvent.get();
        }
        throw new NotFoundException("No organized event with eventId " + eventId);
    }

    public OrganizedEvent changeStatus(long customerId, long eventId, EventStatusEnum status) {
        if (!eventWithIdAndCustomerIdExists(customerId, eventId)) {
            throw new NotFoundException("Event with id " + eventId + " and customer id " + customerId + " does not exist");
        }
        final OrganizedEvent organizedEvent = getWithDetail(eventId);

        switch (status) {
            case IN_PROGRESS: //possible only when current status CONFIRMED
                if (organizedEvent.getEventStatus().equals(CONFIRMED.name())) {
                    organizedEvent.setEventStatus(IN_PROGRESS.name());
                }
                break;
            case CONFIRMED: //if current IN_PROGRESS
                if (possibleToChangeStatusFromInProgressToConfirmed(organizedEvent)) {
                    organizedEvent.setEventStatus(CONFIRMED.name());
                }
                break;
            case READY: //if current CONFIRMED
                if (organizedEvent.getEventStatus().equals(CONFIRMED.name())) {
                    organizedEvent.setEventStatus(READY.name());
                }
                break;
            case CANCELLED:  //any stage except for FINISHED
                //TODO: check if possible to cancel
                organizedEvent.setEventStatus(CANCELLED.name());
                break;
            case FINISHED:
                organizedEvent.setEventStatus(FINISHED.name());
                break;
        }

        save(organizedEvent);

        return organizedEvent;
    }

    private boolean possibleToChangeStatusFromInProgressToConfirmed(OrganizedEvent organizedEvent) {
        final String eventStatus = organizedEvent.getEventStatus();
        if (eventStatus.equals(IN_PROGRESS.name())) {
            if (organizedEvent.getLocationForEvent() == null) {
                return false;
//                throw new IllegalArgumentException("Cannot change status to CONFIRMED while no locations for event chosen");
            } else {
                if (organizedEvent.getLocationForEvent().getConfirmationStatus().equals(ConfirmationStatusEnum.NOT_CONFIRMED.name())) {
//                    throw new IllegalArgumentException("Cannot change status to CONFIRMED while no locations reservation is not confirmed");
                    return false;
                } else {
                    if (organizedEvent.getLocationForEvent().getCateringsForEventLocation() == null &&
                            organizedEvent.getLocationForEvent().getServices() == null) {
                        return true;
                    }
                    if (organizedEvent.getLocationForEvent().getCateringsForEventLocation() != null) {
                        return organizedEvent.getLocationForEvent().getCateringsForEventLocation().stream()
                                .allMatch(catering -> catering.getConfirmationStatus().equals(ConfirmationStatusEnum.CONFIRMED.name()));
                    }
                    if (organizedEvent.getLocationForEvent().getServices() != null) {
                        return organizedEvent.getLocationForEvent().getServices().stream()
                                .allMatch(service -> service.getConfirmationStatus().equals(ConfirmationStatusEnum.CONFIRMED.name()));
                    }
                }
            }
        } else {
            return false;
        }
        return false;
    }


    public List<OrganizedEvent> getAllByCustomerIdAndTab(long customerId, CustomerReservationTabEnum tabEnum) {
        customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("No customer with " + customerId));

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

    private boolean eventWithIdAndCustomerIdExists(long customerId, long eventId) {
        return organizedEventRepository.existsOrganizedEventByIdAndCustomer_Id(eventId, customerId);
    }

    public OrganizedEvent create(long customerId, OrganizedEventDto dto) {
        final Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        final OrganizedEvent organizedEvent = OrganizedEventMapper.fromDto(dto);

        organizedEvent.setEventType(eventTypeService.getByType(dto.getEventType()));
        organizedEvent.setCustomer(customer);
        organizedEvent.setCreatedAt(LocalDateTime.now());
        organizedEvent.setModifiedAt(LocalDateTime.now());

        save(organizedEvent);

        return organizedEvent;
    }

    public void delete(OrganizedEvent organizedEvent) {
        organizedEvent.setModifiedAt(LocalDateTime.now());
        organizedEvent.setDeletedAt(LocalDateTime.now());

        save(organizedEvent);
    }
}
