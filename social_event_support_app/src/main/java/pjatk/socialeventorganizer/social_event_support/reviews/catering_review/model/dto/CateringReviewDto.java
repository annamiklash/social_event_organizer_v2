package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringReviewDto {

    private long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Comment is mandatory")
    @Size(min = 5, max = 100, message
            = "comment should be between 5 and 100 characters")
    private String comment;

    @Min(1)
    @Max(5)
    @NotBlank(message = "Rating is mandatory")
    private int starRating;

    private CustomerDto customer;

    private CateringDto catering;
}
