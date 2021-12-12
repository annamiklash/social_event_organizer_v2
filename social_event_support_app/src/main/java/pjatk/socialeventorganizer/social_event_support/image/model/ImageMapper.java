package pjatk.socialeventorganizer.social_event_support.image.model;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto;

import java.io.Serializable;

@UtilityClass
public class ImageMapper implements Serializable {

    public ImageDto toDto(Image image) {
        return ImageDto.builder()
                .image(image.getImage())
                .isMain(image.isMain())
                .build();
    }

    public LocationImage fromDtoToLocationImage(ImageDto dto) {
        return LocationImage.builder()
                .image(dto.getImage())
                .isMain(dto.isMain())
                .build();
    }

    public CateringImage fromDtoToCateringImage(ImageDto dto) {
        return CateringImage.builder()
                .image(dto.getImage())
                .isMain(dto.isMain())
                .build();
    }

    public OptionalServiceImage fromDtoToServiceImage(ImageDto dto) {
        return OptionalServiceImage.builder()
                .image(dto.getImage())
                .isMain(dto.isMain())
                .build();
    }
}
