package pjatk.socialeventorganizer.social_event_support.reviews.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.dto.CateringReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.dto.LocationReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.OptionalServiceReview;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.dto.ServiceReviewDto;

@UtilityClass
public class ReviewMapper {

    public LocationReview fromLocationReviewDto(LocationReviewDto dto) {
        return LocationReview.builder()
                .title(dto.getTitle())
                .comment(dto.getComment())
                .starRating(dto.getStarRating())
                .build();
    }

    public CateringReview fromCateringReviewDto(CateringReviewDto dto) {
        return CateringReview.builder()
                .title(dto.getTitle())
                .comment(dto.getComment())
                .starRating(dto.getStarRating())
                .build();
    }

    public OptionalServiceReview fromServiceReviewDto(ServiceReviewDto dto) {
        return OptionalServiceReview.builder()
                .title(dto.getTitle())
                .comment(dto.getComment())
                .starRating(dto.getStarRating())
                .build();
    }


    public LocationReviewDto toLocationReviewDto(LocationReview dto) {
        return LocationReviewDto.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .comment(dto.getComment())
                .starRating(dto.getStarRating())
                .build();
    }

    public CateringReviewDto toCateringReviewDto(CateringReview dto) {
        return CateringReviewDto.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .comment(dto.getComment())
                .starRating(dto.getStarRating())
                .build();
    }

    public ServiceReviewDto toServiceReviewDto(OptionalServiceReview dto) {
        return ServiceReviewDto.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .comment(dto.getComment())
                .starRating(dto.getStarRating())
                .build();
    }

}
