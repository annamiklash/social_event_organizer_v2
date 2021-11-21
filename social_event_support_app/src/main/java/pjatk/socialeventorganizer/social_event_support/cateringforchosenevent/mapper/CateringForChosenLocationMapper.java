package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringMapper;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto;

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
