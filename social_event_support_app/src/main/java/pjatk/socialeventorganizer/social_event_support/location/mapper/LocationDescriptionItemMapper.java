package pjatk.socialeventorganizer.social_event_support.location.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.enums.LocationDescriptionItemEnum;
import pjatk.socialeventorganizer.social_event_support.location.model.LocationDescriptionItem;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDescriptionItemDto;

import java.util.stream.Collectors;

@UtilityClass
public class LocationDescriptionItemMapper {

    public LocationDescriptionItemDto toDto(LocationDescriptionItem item) {
        return LocationDescriptionItemDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .build();
    }

    public LocationDescriptionItemDto toDtoWithLocations(LocationDescriptionItem item) {
        final LocationDescriptionItemDto dto = toDto(item);
        dto.setLocations(item.getLocations().stream().map(LocationMapper::toDto).collect(Collectors.toSet()));

        return dto;
    }

    public LocationDescriptionItem fromDto(LocationDescriptionItemDto dto) {
        return LocationDescriptionItem.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .build();
    }

    public LocationDescriptionItemEnum toEnum(LocationDescriptionItemDto dto) {
        return Enum.valueOf(LocationDescriptionItemEnum.class, Converter.capitalizeToEnum(dto.getId()));
    }
}
