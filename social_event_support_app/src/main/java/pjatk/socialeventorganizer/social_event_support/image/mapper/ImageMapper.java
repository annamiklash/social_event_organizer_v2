package pjatk.socialeventorganizer.social_event_support.image.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.image.model.request.ImageRequestDetails;
import pjatk.socialeventorganizer.social_event_support.location.model.LocationImage;

@Component
public class ImageMapper {

    public LocationImage mapToLocationImageDTO(ImageRequestDetails imageRequestDetails, Integer locationId) {
        return LocationImage.builder()
                .image(imageRequestDetails.getImage())
                .alt(imageRequestDetails.getAlt())
                .build();
    }
}
