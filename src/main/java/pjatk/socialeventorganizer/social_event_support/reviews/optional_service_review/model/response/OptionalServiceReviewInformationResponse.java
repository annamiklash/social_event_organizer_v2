package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionalServiceReviewInformationResponse {

    private String title;

    private String comment;

    private Integer optionalServiceId;

    private Integer starRating;

    private Integer customerId;
}
