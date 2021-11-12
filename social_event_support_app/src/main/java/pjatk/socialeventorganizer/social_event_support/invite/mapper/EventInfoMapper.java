package pjatk.socialeventorganizer.social_event_support.invite.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.invite.response.EventInfoResponse;

@Component
public class EventInfoMapper {

    public EventInfoResponse mapToResponse(OrganizedEvent organizedEvent) {

        return EventInfoResponse.builder()
                .eventName(organizedEvent.getName())
                .eventType(organizedEvent.getEventType().getType())
                .startDate(organizedEvent.getStartDate())
                .endDate(organizedEvent.getEndDate())
                .build();
    }
}
