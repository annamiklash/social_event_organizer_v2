package pjatk.socialeventorganizer.social_event_support.user.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.appproblem.mapper.AppProblemMapper;
import pjatk.socialeventorganizer.social_event_support.business.mapper.BusinessMapper;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.common.util.CollectionUtil;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.UserDto;

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
                .createdAt(DateTimeUtil.fromLocalDateTimetoString(user.getCreatedAt()))
                .modifiedAt(DateTimeUtil.fromLocalDateTimetoString(user.getModifiedAt()))
                .deletedAt(DateTimeUtil.fromLocalDateTimetoString(user.getDeletedAt()))
                .blockedAt(DateTimeUtil.fromLocalDateTimetoString(user.getBlockedAt()))
                .isActive(user.isActive())
                .build();
    }

    public UserDto toDtoWithCustomer(User user, Customer customer) {
        final UserDto dto = toDto(user);
        dto.setCustomer(CustomerMapper.toDto(customer));
        return dto;
    }

    public UserDto toDtoWithProblems(User user) {
        final UserDto dto = toDto(user);
        dto.setAppProblems(CollectionUtil.emptyListIfNull(user.getAppProblems())
                .stream()
                .map(AppProblemMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public static UserDto toDtoWithBusiness(User user, Business business) {
        final UserDto dto = toDto(user);
        dto.setBusiness(BusinessMapper.toDto(business));
        return dto;
    }

}
