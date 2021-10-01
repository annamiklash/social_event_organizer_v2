package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringReviewInformationResponse {

    private String title;

    private String comment;

    private Integer cateringId;

    private Integer starRating;

    private Integer customerId;
}