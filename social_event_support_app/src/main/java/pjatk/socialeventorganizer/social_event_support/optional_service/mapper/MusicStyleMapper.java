package pjatk.socialeventorganizer.social_event_support.optional_service.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.MusicStyleDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.music.musicstyle.MusicStyle;

@UtilityClass
public class MusicStyleMapper {

    public MusicStyleDto toDto(MusicStyle musicStyle) {
        return MusicStyleDto.builder()
                .id(musicStyle.getId())
                .name(musicStyle.getName())
                .build();
    }

    public MusicStyle fromDto(MusicStyleDto dto) {
        return MusicStyle.builder()
                .name(dto.getName())
                .build();
    }
}
