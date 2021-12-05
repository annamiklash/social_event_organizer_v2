package pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.mapper.LocationForEventMapper;
import pjatk.socialeventorganizer.social_event_support.optional_service.mapper.OptionalServiceMapper;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.dto.OptionalServiceForChosenLocationDto;

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.NOT_CONFIRMED;

@UtilityClass
public class OptionalServiceForLocationMapper {

    public OptionalServiceForChosenLocation fromDto(OptionalServiceForChosenLocationDto dto) {
        return OptionalServiceForChosenLocation.builder()
                .timeFrom(DateTimeUtil.toLocalTimeFromTimeString(dto.getTimeFrom()))
                .timeTo(DateTimeUtil.toLocalTimeFromTimeString(dto.getTimeTo()))
                .comment(Converter.convertDescriptionsString(dto.getComment()))
                .confirmationStatus(NOT_CONFIRMED.name())
                .build();
    }

    public OptionalServiceForChosenLocationDto toDto(OptionalServiceForChosenLocation optionalService) {
        return OptionalServiceForChosenLocationDto.builder()
                .timeFrom(DateTimeUtil.toTimeOnlyFromLocalTime(optionalService.getTimeFrom()))
                .timeTo(DateTimeUtil.toTimeOnlyFromLocalTime(optionalService.getTimeTo()))
                .comment(optionalService.getComment())
                .confirmationStatus(optionalService.getConfirmationStatus())
                .build();
    }

    public OptionalServiceForChosenLocationDto toDtoWithOptionalService(OptionalServiceForChosenLocation optionalService) {
        return OptionalServiceForChosenLocationDto.builder()
                .timeFrom(DateTimeUtil.toTimeOnlyFromLocalTime(optionalService.getTimeFrom()))
                .timeTo(DateTimeUtil.toTimeOnlyFromLocalTime(optionalService.getTimeTo()))
                .comment(optionalService.getComment())
                .confirmationStatus(optionalService.getConfirmationStatus())
                .optionalService(OptionalServiceMapper.toDto(optionalService.getOptionalService()))
                .build();
    }

    public OptionalServiceForChosenLocationDto toDtoWithOptionalServiceAndLocation(OptionalServiceForChosenLocation optionalService) {
        return OptionalServiceForChosenLocationDto.builder()
                .timeFrom(DateTimeUtil.toTimeOnlyFromLocalTime(optionalService.getTimeFrom()))
                .timeTo(DateTimeUtil.toTimeOnlyFromLocalTime(optionalService.getTimeTo()))
                .comment(optionalService.getComment())
                .confirmationStatus(optionalService.getConfirmationStatus())
                .optionalService(OptionalServiceMapper.toDto(optionalService.getOptionalService()))
                .locationForEvent(LocationForEventMapper.toDto(optionalService.getLocationForEvent()))
                .build();
    }
}
