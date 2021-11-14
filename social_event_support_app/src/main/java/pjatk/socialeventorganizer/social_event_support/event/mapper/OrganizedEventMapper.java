package pjatk.socialeventorganizer.social_event_support.event.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventConfirmationDto;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.locationforevent.mapper.LocationForEventMapper;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.LocationForEvent;

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

    public OrganizedEventConfirmationDto toOrganizedConfirmationDto(LocationForEvent locationForEvent) {
        return OrganizedEventConfirmationDto.builder()
                .id(locationForEvent.getEvent().getId())
                .name(locationForEvent.getEvent().getName())
                .eventType(locationForEvent.getEvent().getEventType().getType())
                .eventStatus(locationForEvent.getEvent().getEventStatus())
                .locationForEvent(LocationForEventMapper.toDto(locationForEvent))
                .build();

    }
}
