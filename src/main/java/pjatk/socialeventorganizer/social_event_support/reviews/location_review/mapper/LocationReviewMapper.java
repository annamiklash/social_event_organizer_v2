package pjatk.socialeventorganizer.social_event_support.reviews.location_review.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.request.LocationReviewRequest;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.response.LocationReviewInformationResponse;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.dto.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.response.LocationReviewResponse;

@Component
public class LocationReviewMapper {

    public LocationReview mapToDTO(LocationReviewRequest request, Integer costumerId) {
        return LocationReview.builder()
                .title(request.getTitle())
                .comment(request.getComment())
                .locationId(request.getLocationId())
                .customerId(costumerId)
                .build();
    }

    public LocationReviewResponse mapToResponse(LocationReview locationReview) {
        return LocationReviewResponse.builder()
                .id(locationReview.getId())
                .title(locationReview.getTitle())
                .comment(locationReview.getComment())
                .build();
    }

    public LocationReviewInformationResponse mapDTOtoLocationReviewInformationResponse(LocationReview LocationReview) {
        return LocationReviewInformationResponse.builder()
                .title(LocationReview.getTitle())
                .comment(LocationReview.getComment())
                .customerId(LocationReview.getCustomerId())
                .locationId(LocationReview.getLocationId())
                .build();
    }
}
