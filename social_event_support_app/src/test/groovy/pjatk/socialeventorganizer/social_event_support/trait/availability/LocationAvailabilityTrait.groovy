package pjatk.socialeventorganizer.social_event_support.trait.availability

import pjatk.socialeventorganizer.social_event_support.availability.AvailabilityEnum
import pjatk.socialeventorganizer.social_event_support.availability.location.model.LocationAvailability
import pjatk.socialeventorganizer.social_event_support.location.model.Location

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

trait LocationAvailabilityTrait {

    LocationAvailability fakeLocationAvailability = LocationAvailability.builder()
            .id(1)
            .date(LocalDate.of(2021, Month.DECEMBER, 31))
            .timeFrom(LocalDateTime.of(2021, Month.DECEMBER, 31, 10, 0, 0))
            .timeTo(LocalDateTime.of(2021, Month.DECEMBER, 31, 23, 0, 0))
            .status(AvailabilityEnum.AVAILABLE.name())
            .location(buildLocation())
            .build()

    def buildLocation() {
        return Location.builder()
                .id(1)
                .name('Name')
                .build();
    }
}
