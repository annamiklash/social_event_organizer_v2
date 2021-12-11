package pjatk.socialeventorganizer.social_event_support.customer.avatar.validator;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.AllowedExtensionsEnum;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.InvalidFileExtensionException;

import java.io.File;

@UtilityClass
public class ImageValidator {

    public void validateFileExtension(String path) {
        if (!path.endsWith(AllowedExtensionsEnum.jpg.name())
                && !path.endsWith(AllowedExtensionsEnum.jpeg.name())) {
            throw new InvalidFileExtensionException("Cannot upload file with given extension");
        }
    }

    public void validateImageSize(File file) {
        long maxAllowedSizeInMb = 2;
        long maxAllowedSizeInBytes = 1024 * 1024 * maxAllowedSizeInMb;

        final long sizeInBytes = file.length();
        if (sizeInBytes > maxAllowedSizeInBytes){
            throw new IllegalArgumentException("Image size cannot be greater than " + maxAllowedSizeInMb + "MB");
        }
    }
}
