package pjatk.socialeventorganizer.social_event_support.trait.event

import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent
import pjatk.socialeventorganizer.social_event_support.location.model.Location

import java.time.LocalDate
import java.time.LocalTime

import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.IN_PROGRESS

trait OrganizedEventTrait {

    OrganizedEvent fakeOrganizedEvent = OrganizedEvent.builder()
            .name("SAMPLE NAME")
            .date(LocalDate.parse('2007-12-03'))
            .startTime(LocalTime.parse("10:00"))
            .endTime(LocalTime.parse("12:00"))
            .guestCount(10)
            .eventStatus(IN_PROGRESS.name())
            .locationForEvent(LocationForEvent.builder()
                    .id(1L)
                    .event(OrganizedEvent.builder()
                            .build())
                    .location(Location.builder().id(2L)
                            .caterings(new HashSet<Catering>())
                            .build())
                    .build())
            .build()
}