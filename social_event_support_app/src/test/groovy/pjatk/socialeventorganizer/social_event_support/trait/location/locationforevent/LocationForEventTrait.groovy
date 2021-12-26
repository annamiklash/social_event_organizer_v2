package pjatk.socialeventorganizer.social_event_support.trait.location.locationforevent

import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation

import java.time.LocalTime

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.CONFIRMED

trait LocationForEventTrait {

    LocationForEvent fakeLocationForEvent = LocationForEvent.builder()
            .id(1L)
            .timeFrom(LocalTime.parse("10:00:00"))
            .timeTo(LocalTime.parse("12:00:00"))
            .guestCount(10)
            .confirmationStatus(CONFIRMED.name())
            .cateringsForEventLocation(new HashSet<CateringForChosenEventLocation>())
            .services(new HashSet<OptionalServiceForChosenLocation>())
            .build()

}