package pjatk.socialeventorganizer.social_event_support.trait.availability

import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto

trait AvailabilityTrait {

    AvailabilityDto fakeAvailabilityDto = AvailabilityDto.builder()
            .date('2021-12-31')
            .timeFrom('10:00:00')
            .timeTo('23:00:00')
            .build()

}