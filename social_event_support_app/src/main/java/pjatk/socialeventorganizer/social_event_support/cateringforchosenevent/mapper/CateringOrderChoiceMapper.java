package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringItemMapper;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringOrderChoice;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringOrderChoiceDto;

@UtilityClass
public class CateringOrderChoiceMapper {

    public CateringOrderChoice fromDto(CateringOrderChoiceDto dto) {
        return CateringOrderChoice.builder()
                .amount(dto.getAmount())
                .build();
    }

    public CateringOrderChoiceDto toDto(CateringOrderChoice cateringOrderChoice) {
        return CateringOrderChoiceDto.builder()
                .id(cateringOrderChoice.getId())
                .amount(cateringOrderChoice.getAmount())
                .build();
    }

    public CateringOrderChoiceDto toDtoWithItem(CateringOrderChoice cateringOrderChoice) {
        final CateringOrderChoiceDto dto = toDto(cateringOrderChoice);
        dto.setItem(CateringItemMapper.toDto(cateringOrderChoice.getItem()));

        return dto;
    }
}
