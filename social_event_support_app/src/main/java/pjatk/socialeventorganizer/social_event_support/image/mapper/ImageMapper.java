package pjatk.socialeventorganizer.social_event_support.image.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.image.model.Image;
import pjatk.socialeventorganizer.social_event_support.image.model.LocationImage;
import pjatk.socialeventorganizer.social_event_support.image.model.request.ImageDto;

@UtilityClass
public class ImageMapper {

    public Image fromDto(ImageDto imageDto) {
        return LocationImage.builder()
                .isMain(imageDto.isMain())
                .type(imageDto.getType())
                .build();
    }

    public ImageDto toDto(Image image) {
        return ImageDto.builder()
                .isMain(image.isMain())
                .type(image.getType())
                .image(image.getImage())
                .build();
    }



}
