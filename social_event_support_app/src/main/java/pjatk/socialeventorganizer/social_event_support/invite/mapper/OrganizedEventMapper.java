package pjatk.socialeventorganizer.social_event_support.invite.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.invite.response.EventInfoResponse;

@UtilityClass
public class OrganizedEventMapper {

    public EventInfoResponse mapToResponse(OrganizedEvent organizedEvent) {

        return EventInfoResponse.builder()
                .eventName(organizedEvent.getName())
                .eventType(organizedEvent.getEventType().getType())
                .startDate(organizedEvent.getStartDate())
                .endDate(organizedEvent.getEndDate())
                .build();
    }

}
