package pjatk.socialeventorganizer.social_event_support.image.util;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.validator.ImageValidator;

import java.io.File;

@UtilityClass
public class ImageUtil {

    public byte[] fromPathToByteArray(String path) {
        final File file = new File(path);
        ImageValidator.validateImageSize(file);

        return new byte[(int) file.length()];
    }
}
