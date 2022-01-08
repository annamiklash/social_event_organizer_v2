package pjatk.socialeventorganizer.social_event_support.catering.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringItemDto;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;

@UtilityClass
public class CateringItemMapper {

    public CateringItem fromDto(CateringItemDto dto) {
        return CateringItem.builder()
                .name(dto.getName())
                .description(Converter.convertDescriptionsString(dto.getDescription()))
                .servingPrice(Converter.convertPriceString(dto.getServingPrice()))
                .itemType(dto.getType())
                .isVegan(Boolean.TRUE.equals(dto.isVegan()))
                .isVegetarian(Boolean.TRUE.equals(dto.isVegetarian()))
                .isGlutenFree(Boolean.TRUE.equals(dto.isGlutenFree()))
                .createdAt(Converter.fromStringToFormattedDateTime(dto.getCreatedAt()))
                .modifiedAt(Converter.fromStringToFormattedDateTime(dto.getModifiedAt()))
                .build();
    }

    public CateringItemDto toDto(CateringItem cateringItem) {
        return CateringItemDto.builder()
                .id(cateringItem.getId())
                .name(cateringItem.getName())
                .servingPrice(String.valueOf(cateringItem.getServingPrice()))
                .type(cateringItem.getItemType())
                .description(cateringItem.getDescription())
                .isVegan(Boolean.TRUE.equals(cateringItem.isVegan()))
                .isVegetarian(Boolean.TRUE.equals(cateringItem.isVegetarian()))
                .isGlutenFree(Boolean.TRUE.equals(cateringItem.isGlutenFree()))
                .createdAt(String.valueOf(cateringItem.getCreatedAt()))
                .modifiedAt(String.valueOf(cateringItem.getModifiedAt()))
                .build();

    }
}
