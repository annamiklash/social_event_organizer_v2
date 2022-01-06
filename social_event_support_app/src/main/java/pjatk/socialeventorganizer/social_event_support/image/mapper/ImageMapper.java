package pjatk.socialeventorganizer.social_event_support.image.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.util.Base64Utils;
import pjatk.socialeventorganizer.social_event_support.image.model.CateringImage;
import pjatk.socialeventorganizer.social_event_support.image.model.Image;
import pjatk.socialeventorganizer.social_event_support.image.model.LocationImage;
import pjatk.socialeventorganizer.social_event_support.image.model.OptionalServiceImage;
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto;

@UtilityClass
public class ImageMapper {

    public LocationImage fromDtoToLocationImage(ImageDto imageDto) {
        return LocationImage.builder()
                .isMain(imageDto.isMain())
                .fileName(imageDto.getName())
                .build();
    }

    public OptionalServiceImage fromDtoToServiceImage(ImageDto imageDto) {
        return OptionalServiceImage.builder()
                .isMain(imageDto.isMain())
                .fileName(imageDto.getName())
                .build();
    }

    public CateringImage fromDtoToCateringImage(ImageDto imageDto) {
        return CateringImage.builder()
                .isMain(imageDto.isMain())
                .fileName(imageDto.getName())
                .build();
    }

    public ImageDto toDto(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .isMain(image.isMain())
                .name(image.getFileName())
                .encodedImage(Base64Utils.encodeToString(image.getImage()))
                .build();
    }
}
