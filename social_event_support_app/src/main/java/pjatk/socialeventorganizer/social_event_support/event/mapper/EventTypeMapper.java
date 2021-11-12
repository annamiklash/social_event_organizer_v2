package pjatk.socialeventorganizer.social_event_support.event.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.event.model.EventType;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.EventTypeDto;

@UtilityClass
public class EventTypeMapper {

    public EventTypeDto toDto(EventType eventType) {
        return EventTypeDto.builder()
                .id(eventType.getId())
                .type(eventType.getType())
                .description(eventType.getDescription())
                .createdAt(String.valueOf(eventType.getCreatedAt()))
                .build();
    }

    public EventType fromDto(EventTypeDto dto) {
        return EventType.builder()
                .type(dto.getType())
                .description(dto.getDescription())
                .createdAt(Converter.fromStringToFormattedDateTime(dto.getCreatedAt()))
                .build();
    }
}
