package pjatk.socialeventorganizer.social_event_support.customer.avatar.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.CustomerAvatar;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.dto.CustomerAvatarDto;

@UtilityClass
public class CustomerAvatarMapper {

    private static final String NAME = "AVATAR";

    public CustomerAvatar fromDto(CustomerAvatarDto dto) {
        return CustomerAvatar.builder()
                .image(dto.getImage())
                .fileName(NAME)
                .build();
    }
}
