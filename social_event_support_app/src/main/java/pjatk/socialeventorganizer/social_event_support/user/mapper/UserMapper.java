package pjatk.socialeventorganizer.social_event_support.user.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.appproblem.mapper.AppProblemMapper;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.UserDto;

import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public User fromDto(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .type(dto.getType())
                .createdAt(Converter.fromStringToFormattedDateTime(dto.getCreatedAt()))
                .modifiedAt(Converter.fromStringToFormattedDateTime(dto.getModifiedAt()))
                .deletedAt(null)
                .isActive(dto.isActive())
                .blockedAt(null)
                .build();

    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .type(user.getType())
                .createdAt(String.valueOf(user.getCreatedAt()))
                .modifiedAt(String.valueOf(user.getModifiedAt()))
                .deletedAt(String.valueOf(user.getDeletedAt()))
                .blockedAt(String.valueOf(user.getBlockedAt()))
                .isActive(user.isActive())
                .build();
    }

    public UserDto toDtoWithProblems(User user) {
        final UserDto dto = toDto(user);
        dto.setAppProblems(user.getAppProblems().stream().map(AppProblemMapper::toDto).collect(Collectors.toSet()));

        return dto;
    }
}
