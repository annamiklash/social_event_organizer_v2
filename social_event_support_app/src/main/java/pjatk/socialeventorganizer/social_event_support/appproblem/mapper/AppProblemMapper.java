package pjatk.socialeventorganizer.social_event_support.appproblem.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.appproblem.AppProblem;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblemDto;
import pjatk.socialeventorganizer.social_event_support.user.mapper.UserMapper;

@UtilityClass
public class AppProblemMapper {

    public AppProblemDto toDto(AppProblem appProblem) {
        return AppProblemDto.builder()
                .id(appProblem.getId())
                .createdAt(String.valueOf(appProblem.getCreatedAt()))
                .concern(appProblem.getConcern())
                .description(appProblem.getDescription())
                .build();
    }

    public AppProblemDto toDtoWithUser(AppProblem appProblem) {
        final AppProblemDto appProblemDto = toDto(appProblem);
        appProblemDto.setUser(UserMapper.toDto(appProblem.getUser()));
        return appProblemDto;
    }

    public AppProblem fromDto(AppProblemDto dto) {
        return AppProblem.builder()
                .concern(dto.getConcern())
                .description(dto.getDescription())
                .build();
    }
}
