package pjatk.socialeventorganizer.social_event_support.event.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;

@UtilityClass
public class OrganizedEventMapper {

    public OrganizedEventDto toDto(OrganizedEvent organizedEvent) {
        return OrganizedEventDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .startDate(String.valueOf(organizedEvent.getStartDate()))
                .endDate(String.valueOf(organizedEvent.getEndDate()))
                .isPredefined(organizedEvent.getIsPredefined())
                .eventStatus(organizedEvent.getEventStatus())
                .eventType(EventTypeMapper.toDto(organizedEvent.getEventType()))
                .build();
    }

    public OrganizedEventDto toDtoWithCustomer(OrganizedEvent organizedEvent) {
        return OrganizedEventDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .startDate(String.valueOf(organizedEvent.getStartDate()))
                .endDate(String.valueOf(organizedEvent.getEndDate()))
                .isPredefined(organizedEvent.getIsPredefined())
                .eventStatus(organizedEvent.getEventStatus())
                .eventType(EventTypeMapper.toDto(organizedEvent.getEventType()))
                .customer(CustomerMapper.toDto(organizedEvent.getCustomer()))
                .build();
    }
}
