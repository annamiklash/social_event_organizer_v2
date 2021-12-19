package pjatk.socialeventorganizer.social_event_support.trait


import pjatk.socialeventorganizer.social_event_support.businesshours.DayEnum
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto

trait BusinessHoursTrait {

    BusinessHoursDto fakeBusinessHoursDto = BusinessHoursDto.builder()
            .id(1)
            .day(DayEnum.MONDAY)
            .timeFrom('10')
            .timeTo('20')
            .build()

    CateringBusinessHours fakeCateringBusinessHours = CateringBusinessHours.builder()
            .id(1)
            .day(DayEnum.MONDAY.name())
            .build()

}