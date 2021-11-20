package pjatk.socialeventorganizer.social_event_support.location.availability.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.location.availability.model.LocationAvailability;
import pjatk.socialeventorganizer.social_event_support.location.availability.model.dto.LocationAvailabilityDto;

@UtilityClass
public class LocationAvailabilityMapper {

    public LocationAvailabilityDto toDto(LocationAvailability availability) {
        return LocationAvailabilityDto.builder()
                .id(availability.getId())
                .date(DateTimeUtil.toDateOnlyStringFromLocalDateTime(availability.getDate()))
                .timeFrom(DateTimeUtil.toTimeOnlyStringFromLocalDateTime(availability.getTimeFrom()))
                .timeTo(DateTimeUtil.toTimeOnlyStringFromLocalDateTime(availability.getTimeTo()))
                .status(availability.getStatus())
                .build();
    }

    public LocationAvailability fromDto(LocationAvailabilityDto dto) {
        return LocationAvailability.builder()
                .date(DateTimeUtil.parseFromString(dto.getDate()))
                .timeFrom(DateTimeUtil.fromStringToFormattedDateTime(dto.getDate() + " " + dto.getTimeFrom()))
                .timeTo(DateTimeUtil.fromStringToFormattedDateTime(dto.getDate() + " " + dto.getTimeTo()))
                .status(dto.getStatus())
                .build();
    }
}
