package pjatk.socialeventorganizer.social_event_support.catering.cateringforchosenevent.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.catering.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.catering.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringMapper;

@UtilityClass
public class CateringForChosenLocationMapper {

    public CateringForChosenEventLocationDto toDto(CateringForChosenEventLocation catering) {
        return CateringForChosenEventLocationDto.builder()
                .id(catering.getId())
                .dateTime(String.valueOf(catering.getDateTime()))
                .catering(CateringMapper.toDto(catering.getCatering()))
                .build();
    }

    //TODO: finish
    public CateringForChosenEventLocation fromDto(CateringForChosenEventLocationDto dto) {

        return CateringForChosenEventLocation.builder()
                .build();
    }
}
