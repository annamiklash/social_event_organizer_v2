package pjatk.socialeventorganizer.social_event_support.user.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.appproblem.mapper.AppProblemMapper;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.UserDto;

import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public User fromDto(UserDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .type(dto.getType())
                .isActive(dto.isActive())
                .deletedAt(null)
                .blockedAt(null)
                .build();

    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .type(user.getType())
                .createdAt(DateTimeUtil.toStringFromLocalDateTime(user.getCreatedAt()))
                .modifiedAt(DateTimeUtil.toStringFromLocalDateTime(user.getModifiedAt()))
                .deletedAt(DateTimeUtil.toStringFromLocalDateTime(user.getDeletedAt()))
                .blockedAt(DateTimeUtil.toStringFromLocalDateTime(user.getBlockedAt()))
                .isActive(user.isActive())
                .build();
    }

    public UserDto toDtoWithProblems(User user) {
        final UserDto dto = toDto(user);
        dto.setAppProblems(user.getAppProblems().stream().map(AppProblemMapper::toDto).collect(Collectors.toSet()));

        return dto;
    }
}
