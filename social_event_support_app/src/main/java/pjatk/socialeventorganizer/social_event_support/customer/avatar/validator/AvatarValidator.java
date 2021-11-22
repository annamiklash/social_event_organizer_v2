package pjatk.socialeventorganizer.social_event_support.customer.avatar.validator;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.AllowedExtensionsEnum;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.dto.CustomerAvatarDto;
import pjatk.socialeventorganizer.social_event_support.exceptions.InvalidFileExtensionException;

@UtilityClass
public class AvatarValidator {

    public void validate(CustomerAvatarDto dto) {
        final String path = dto.getPath();

        if (!path.endsWith(AllowedExtensionsEnum.jpg.name())
                && !path.endsWith(AllowedExtensionsEnum.jpeg.name())) {
            throw new InvalidFileExtensionException("Cannot upload file with given extension");
        }
    }
}
