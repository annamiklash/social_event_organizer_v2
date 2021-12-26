package pjatk.socialeventorganizer.social_event_support.trait.event

import pjatk.socialeventorganizer.social_event_support.event.model.EventType
import pjatk.socialeventorganizer.social_event_support.event.model.dto.EventTypeDto

trait EventTypeTrait {

    EventType fakeEventType = EventType.builder()
            .id(1)
            .type('Party')
            .build();

    EventTypeDto fakeEventTypeDto = EventTypeDto.builder()
            .type('Party')
            .build();

}