package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionalServiceReviewResponse {

    private Long id;
    private String title;
    private String comment;
    private Integer starRating;
}


