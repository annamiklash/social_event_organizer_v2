package pjatk.socialeventorganizer.social_event_support.image.model;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.image.model.request.ImageRequestDetails;
import pjatk.socialeventorganizer.social_event_support.location.model.LocationImage;

import java.io.Serializable;

@UtilityClass
public class ImageMapper implements Serializable {

    public ImageRequestDetails toDto(LocationImage locationImage) {
        return ImageRequestDetails.builder()
                .image(locationImage.getImage())
                .alt(locationImage.getAlt())
                .build();
    }
}
