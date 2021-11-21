package pjatk.socialeventorganizer.social_event_support.businesshours.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.businesshours.BusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.DayEnum;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto;
import pjatk.socialeventorganizer.social_event_support.businesshours.location.model.LocationBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.model.OptionalServiceBusinessHours;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;

import java.time.LocalTime;

@UtilityClass
public class BusinessHoursMapper {

    public BusinessHoursDto toDto(BusinessHours businessHours) {
        return BusinessHoursDto.builder()
                .id(businessHours.getId())
                .day(DayEnum.valueOf(businessHours.getDay()))
                .timeFrom(DateTimeUtil.toTomeOnlyFromLocalTime(businessHours.getTimeFrom()))
                .timeTo(DateTimeUtil.toTomeOnlyFromLocalTime(businessHours.getTimeTo()))
                .build();
    }

    public LocationBusinessHours fromDtoToLocation(BusinessHoursDto dto) {
        return LocationBusinessHours.builder()
                .day(dto.getDay().name())
                .timeFrom(DateTimeUtil.test(dto.getTimeFrom()))
                .timeTo(DateTimeUtil.test(dto.getTimeTo()))
                .build();
    }

    public CateringBusinessHours fromDtoToCatering(BusinessHoursDto dto){
        return CateringBusinessHours.builder()
                .day(dto.getDay().name())
                .timeFrom(LocalTime.parse(dto.getTimeFrom()))
                .timeTo(LocalTime.parse(dto.getTimeTo()))
                .build();
    }

    public OptionalServiceBusinessHours fromDtoToOptionalService(BusinessHoursDto dto){
        return OptionalServiceBusinessHours.builder()
                .day(dto.getDay().name())
                .timeFrom(LocalTime.parse(dto.getTimeFrom()))
                .timeTo(LocalTime.parse(dto.getTimeTo()))
                .build();
    }

}
