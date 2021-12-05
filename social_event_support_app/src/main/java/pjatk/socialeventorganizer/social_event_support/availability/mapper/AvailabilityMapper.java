package pjatk.socialeventorganizer.social_event_support.availability.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.availability.Availability;
import pjatk.socialeventorganizer.social_event_support.availability.catering.model.CateringAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.availability.location.model.LocationAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;

@UtilityClass
public class AvailabilityMapper {

    public AvailabilityDto toDto(Availability availability) {
        return AvailabilityDto.builder()
                .id(availability.getId())
                .date(DateTimeUtil.toDateOnlyStringFromLocalDateTime(availability.getDate()))
                .timeFrom(DateTimeUtil.toTimeOnlyStringFromLocalDateTime(availability.getTimeFrom()))
                .timeTo(DateTimeUtil.toTimeOnlyStringFromLocalDateTime(availability.getTimeTo()))
                .status(availability.getStatus())
                .build();
    }

    public LocationAvailability fromDtoToLocationAvailability(AvailabilityDto dto) {
        return LocationAvailability.builder()
                .date(DateTimeUtil.parseFromString(dto.getDate()))
                .timeFrom(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())))
                .timeTo(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo())))
                .status(dto.getStatus())
                .build();
    }

    public CateringAvailability fromDtoToCateringAvailability(AvailabilityDto dto) {
        return CateringAvailability.builder()
                .date(DateTimeUtil.parseFromString(dto.getDate()))
                .timeFrom(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())))
                .timeTo(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo())))
                .status(dto.getStatus())
                .build();
    }
    public OptionalServiceAvailability fromDtoToOptionalServiceAvailability(AvailabilityDto dto) {
        return OptionalServiceAvailability.builder()
                .date(DateTimeUtil.parseFromString(dto.getDate()))
                .timeFrom(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())))
                .timeTo(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo())))
                .status(dto.getStatus())
                .build();
    }


}
