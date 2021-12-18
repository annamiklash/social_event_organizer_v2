package pjatk.socialeventorganizer.social_event_support.trait.location

import pjatk.socialeventorganizer.social_event_support.businesshours.DayEnum
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto

trait LocationTrait {

    BusinessHoursDto fakeBusinessHoursDto = BusinessHoursDto.builder()
            .id(1)
            .day(DayEnum.MONDAY)
            .timeFrom('10')
            .timeTo('20')
            .build()

}