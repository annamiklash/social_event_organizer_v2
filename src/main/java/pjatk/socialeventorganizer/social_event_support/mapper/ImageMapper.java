package pjatk.socialeventorganizer.social_event_support.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.model.dto.LocationImage;
import pjatk.socialeventorganizer.social_event_support.model.request.ImageRequestDetails;

@Component
public class ImageMapper {

    public LocationImage mapToLocationImageDTO(ImageRequestDetails imageRequestDetails, Integer locationId) {
        return LocationImage.builder()
                .locationId(locationId)
                .image(imageRequestDetails.getImage())
                .alt(imageRequestDetails.getAlt())
                .build();
    }
}
