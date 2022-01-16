package pjatk.socialeventorganizer.social_event_support.trait.availability

import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

trait AvailabilityTrait {

    AvailabilityDto fakeAvailabilityDto = AvailabilityDto.builder()
            .date('2021-12-31')
            .timeFrom('10:00:00')
            .timeTo('23:00:00')
            .build()

    OptionalServiceAvailability fakeServiceAvailability = OptionalServiceAvailability.builder()
            .date(LocalDate.of(2022, Month.JANUARY, 1))
            .timeFrom(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 0, 0))
            .timeTo(LocalDateTime.of(2022, Month.JANUARY, 1, 20, 0, 0))
            .build()

}