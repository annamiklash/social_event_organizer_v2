package pjatk.socialeventorganizer.social_event_support.image.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.util.Base64Utils;
import pjatk.socialeventorganizer.social_event_support.image.model.Image;
import pjatk.socialeventorganizer.social_event_support.image.model.LocationImage;
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto;

@UtilityClass
public class ImageMapper {

    public Image fromDto(ImageDto imageDto) {
        return LocationImage.builder()
                .isMain(imageDto.isMain())
                .name(imageDto.getName())
                .build();
    }

    public ImageDto toDto(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .isMain(image.isMain())
                .name(image.getName())
                .encodedImage(Base64Utils.encodeToString(image.getImage()))
                .build();
    }
}
