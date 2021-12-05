package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringMapper;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.NOT_CONFIRMED;

@UtilityClass
public class CateringForChosenLocationMapper {

    public CateringForChosenEventLocationDto toDto(CateringForChosenEventLocation catering) {
        return CateringForChosenEventLocationDto.builder()
                .id(catering.getId())
                .time(DateTimeUtil.toTimeOnlyFromLocalTime(catering.getTime()))
                .comment(catering.getComment())
                .confirmationStatus(catering.getConfirmationStatus())
                .catering(CateringMapper.toDto(catering.getCatering()))
                .build();
    }

    public CateringForChosenEventLocation fromDto(CateringForChosenEventLocationDto dto) {
        return CateringForChosenEventLocation.builder()
                .time(DateTimeUtil.toLocalTimeFromTimeString(dto.getTime()))
                .comment(Converter.convertDescriptionsString(dto.getComment()))
                .confirmationStatus(NOT_CONFIRMED.name())
                .build();
    }
}
