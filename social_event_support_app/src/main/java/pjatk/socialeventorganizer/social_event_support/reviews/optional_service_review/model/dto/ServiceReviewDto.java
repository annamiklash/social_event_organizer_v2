package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceReviewDto {

    private long id;

    @NotNull
    private String title;

    @NotNull
    private String comment;

    @NotNull
    private int starRating;

    private CustomerDto customer;

    private OptionalServiceDto service;
}
