package pjatk.socialeventorganizer.social_event_support.trait


import pjatk.socialeventorganizer.social_event_support.businesshours.DayEnum
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto
import pjatk.socialeventorganizer.social_event_support.businesshours.location.model.LocationBusinessHours
import pjatk.socialeventorganizer.social_event_support.businesshours.service.model.OptionalServiceBusinessHours

import java.time.LocalTime

trait BusinessHoursTrait {

    BusinessHoursDto fakeBusinessHoursDto = BusinessHoursDto.builder()
            .day(DayEnum.MONDAY)
            .timeFrom('10:00:00')
            .timeTo('20:00:00')
            .build()

    CateringBusinessHours fakeCateringBusinessHours = CateringBusinessHours.builder()
            .day(DayEnum.MONDAY.name())
            .timeFrom(LocalTime.of(10, 0))
            .timeTo(LocalTime.of(20, 0))
            .build()

    CateringBusinessHours fakeCateringBusinessHoursWithId = CateringBusinessHours.builder()
            .id(1)
            .day(DayEnum.MONDAY.name())
            .timeFrom(LocalTime.of(10, 0))
            .timeTo(LocalTime.of(20, 0))
            .build()

    LocationBusinessHours fakeLocationBusinessHours = LocationBusinessHours.builder()
            .day(DayEnum.MONDAY.name())
            .timeFrom(LocalTime.of(10, 0))
            .timeTo(LocalTime.of(20, 0))
            .build()

    LocationBusinessHours fakeLocationBusinessHoursWithId = LocationBusinessHours.builder()
            .id(1)
            .day(DayEnum.MONDAY.name())
            .timeFrom(LocalTime.of(10, 0))
            .timeTo(LocalTime.of(20, 0))
            .build()

    OptionalServiceBusinessHours fakeServiceBusinessHours = OptionalServiceBusinessHours.builder()
            .day(DayEnum.MONDAY.name())
            .timeFrom(LocalTime.of(10, 0))
            .timeTo(LocalTime.of(20, 0))
            .build()

    OptionalServiceBusinessHours fakeServiceBusinessHoursWithId = OptionalServiceBusinessHours.builder()
            .id(1)
            .day(DayEnum.MONDAY.name())
            .timeFrom(LocalTime.of(10, 0))
            .timeTo(LocalTime.of(20, 0))
            .build()

}