package pjatk.socialeventorganizer.social_event_support.reviews.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.reviews.Review;
import pjatk.socialeventorganizer.social_event_support.reviews.ReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.catering.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.location.model.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.service.model.OptionalServiceReview;

@UtilityClass
public class ReviewMapper {

    public LocationReview fromLocationReviewDto(ReviewDto dto) {
        return LocationReview.builder()
                .title(dto.getTitle())
                .comment(dto.getComment())
                .starRating(dto.getStarRating())
                .build();
    }

    public CateringReview fromCateringReviewDto(ReviewDto dto) {
        return CateringReview.builder()
                .title(dto.getTitle())
                .comment(dto.getComment())
                .starRating(dto.getStarRating())
                .build();
    }

    public OptionalServiceReview fromServiceReviewDto(ReviewDto dto) {
        return OptionalServiceReview.builder()
                .title(dto.getTitle())
                .comment(dto.getComment())
                .starRating(dto.getStarRating())
                .build();
    }

    public ReviewDto toDto(Review locationReview) {
        return ReviewDto.builder()
                .id(locationReview.getId())
                .title(locationReview.getTitle())
                .comment(locationReview.getComment())
                .starRating(locationReview.getStarRating())
                .customer(CustomerMapper.toDto(locationReview.getCustomer()))
                .build();
    }

}
