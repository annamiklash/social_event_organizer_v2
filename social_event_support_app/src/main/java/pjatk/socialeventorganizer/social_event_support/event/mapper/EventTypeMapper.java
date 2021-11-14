package pjatk.socialeventorganizer.social_event_support.event.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.event.model.EventType;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.EventTypeDto;

@UtilityClass
public class EventTypeMapper {

    public EventTypeDto toDto(EventType eventType) {
        return EventTypeDto.builder()
                .id(eventType.getId())
                .type(eventType.getType())
                .build();
    }

    public EventType fromDto(EventTypeDto dto) {
        return EventType.builder()
                .type(dto.getType())
                .build();
    }
}
