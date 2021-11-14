package pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationReviewDto {

    private long id;

    @NotNull
    private String title;

    @NotNull
    private String comment;

    @NotNull
    private int starRating;

    private CustomerDto customer;

    private LocationDto location;
}
