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
import pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.event.repository.OrganizedEventRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.util.Optional;

import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrganizedEventService {

    private final OrganizedEventRepository organizedEventRepository;

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

    public void save(OrganizedEvent organizedEvent) {
        organizedEventRepository.save(organizedEvent);
    }

    public OrganizedEvent getWithAllInformationForSendingInvitations(long id) {
        final Optional<OrganizedEvent> organizedEvent = organizedEventRepository.getWithAllInformationForSendingInvitations(id);

        if (organizedEvent.isPresent()) {
            return organizedEvent.get();
        }
        throw new NotFoundException("No organized event with id " + id);
    }

    public OrganizedEvent changeStatus(long customerId, long eventId, EventStatusEnum status) {
        if (!eventWithIdAndCustomerIdExists(customerId, eventId)) {
            throw new NotFoundException("Event with id " + eventId + " and customer id " + customerId + " does not exist");
        }
        final OrganizedEvent organizedEvent = get(eventId);

        switch (status) {
            case IN_PROGRESS:
                organizedEvent.setEventStatus(IN_PROGRESS.toString());
                break;
            case CONFIRMED:
                organizedEvent.setEventStatus(CONFIRMED.toString());
                break;
            case READY:
                organizedEvent.setEventStatus(READY.toString());
                break;
            case CANCELLED:
                //TODO: check if possible to cancel
                organizedEvent.setEventStatus(CANCELLED.toString());
                break;
            case FINISHED:
                organizedEvent.setEventStatus(FINISHED.toString());
                break;
        }

        save(organizedEvent);

        return organizedEvent;
    }


    private boolean eventWithIdAndCustomerIdExists(long customerId, long eventId) {
        return organizedEventRepository.existsOrganizedEventByIdAndCustomer_Id(customerId, eventId);
    }
}
