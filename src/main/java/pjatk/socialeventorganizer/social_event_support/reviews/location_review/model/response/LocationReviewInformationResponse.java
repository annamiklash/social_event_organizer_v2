package pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationReviewInformationResponse {

    private String title;

    private String comment;

    private Integer locationId;

    private Integer starRating;

    private Integer customerId;
}
