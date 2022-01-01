package pjatk.socialeventorganizer.social_event_support.trait.cateringforchosenevent


import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent

import java.time.LocalDate
import java.time.LocalTime

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.CONFIRMED
import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.NOT_CONFIRMED

trait CateringForChosenEventLocationTrait {

    CateringForChosenEventLocation fakeCateringForChosenEventLocation = CateringForChosenEventLocation.builder()
            .time(LocalTime.parse('10:15'))
            .comment("SAMPLE COMMENT")
            .confirmationStatus(NOT_CONFIRMED.name())
            .eventLocation(LocationForEvent.builder()
                    .id(1L)
                    .event(OrganizedEvent.builder()
                            .date(LocalDate.parse('2022-12-03'))
                            .build())
                    .build())
            .build()

    CateringForChosenEventLocationDto fakeCateringForChosenEventLocationDto = CateringForChosenEventLocationDto.builder()
            .id(1L)
            .time('10:15')
            .comment("SAMPLE COMMENT")
            .confirmationStatus(CONFIRMED.name())
            .build()
}