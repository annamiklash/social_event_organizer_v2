package pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationReviewRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location_review")
    Long id;

    @NotBlank(message = "Title for the review is mandatory")
    @Size(min = 1, max = 100, message = "The title should be between 1 and 100 characters")
    private String title;

    @NotBlank(message = "Leaving a comment to the review is mandatory")
    @Size(min = 1, max = 300, message
            = "Your review comment should be between 1 and 300 characters")
    private String comment;

    @NotNull
    @Min(value = 1, message = "Rating in stars can be from 1 to 5.")
    @Max(value = 5, message = "Rating in stars can be from 1 to 5.")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer starRating;

    @NotNull
    private Integer locationId;


}
