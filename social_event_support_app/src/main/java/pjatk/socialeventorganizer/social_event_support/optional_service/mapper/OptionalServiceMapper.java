package pjatk.socialeventorganizer.social_event_support.optional_service.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.business.mapper.BusinessMapper;
import pjatk.socialeventorganizer.social_event_support.businesshours.mapper.BusinessHoursMapper;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto;

import java.util.stream.Collectors;

@UtilityClass
public class OptionalServiceMapper {

    public OptionalServiceDto toDto(OptionalService optionalService) {
        return OptionalServiceDto.builder()
                .id(optionalService.getId())
                .type(optionalService.getType())
                .alias(optionalService.getAlias())
                .description(optionalService.getDescription())
                .serviceCost(String.valueOf(optionalService.getServiceCost()))
                .email(optionalService.getEmail())
                .createdAt(DateTimeUtil.toStringFromLocalDateTime(optionalService.getCreatedAt()))
                .modifiedAt(DateTimeUtil.toStringFromLocalDateTime(optionalService.getModifiedAt()))
                .deletedAt(DateTimeUtil.toStringFromLocalDateTime(optionalService.getDeletedAt()))
                .build();
    }

    public OptionalService fromDto(OptionalServiceDto dto) {
        return OptionalService.builder()
                .type(dto.getType())
                .alias(dto.getAlias())
                .description(dto.getDescription())
                .serviceCost(Converter.convertPriceString(dto.getServiceCost()))
                .email(dto.getEmail())
                .build();
    }

    public OptionalServiceDto toDtoWithDetails(OptionalService optionalService) {
        final OptionalServiceDto dto = toDto(optionalService);
        dto.setBusiness(BusinessMapper.toDto(optionalService.getBusiness()));

        dto.setBusinessHours(optionalService.getOptionalServiceBusinessHours().stream()
                .map(BusinessHoursMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }
}
