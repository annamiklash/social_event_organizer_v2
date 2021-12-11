package pjatk.socialeventorganizer.social_event_support.image.model;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.image.model.request.ImageDto;

import java.io.Serializable;

@UtilityClass
public class ImageMapper implements Serializable {

    public ImageDto toDto(Image image) {
        return ImageDto.builder()
                .image(image.getImage())
                .isMain(image.isMain())
                .build();
    }

    public Image fromDto(ImageDto dto) {
        return Image.builder()
                .image(dto.getImage())
                .isMain(dto.isMain())
                .build();
    }
}
