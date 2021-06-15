package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.dto.OptionalServiceReview;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.request.OptionalServiceReviewRequest;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.response.OptionalServiceReviewInformationResponse;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.response.OptionalServiceReviewResponse;

@Component
public class OptionalServiceReviewMapper {

    public OptionalServiceReview mapToDTO(OptionalServiceReviewRequest request, Integer costumerId) {
        return OptionalServiceReview.builder()
                .title(request.getTitle())
                .comment(request.getComment())
                .starRating(request.getStarRating())
                .optionalServiceId(request.getOptionalServiceId())
                .customerId(costumerId)
                .build();
    }

    public OptionalServiceReviewResponse mapToResponse(OptionalServiceReview optionalServiceReview) {
        return OptionalServiceReviewResponse.builder()
                .id(optionalServiceReview.getId())
                .title(optionalServiceReview.getTitle())
                .starRating(optionalServiceReview.getStarRating())
                .comment(optionalServiceReview.getComment())
                .build();
    }

    public OptionalServiceReviewInformationResponse mapDTOtoOptionalServiceReviewInformationResponse(OptionalServiceReview OptionalServiceReview) {
        return OptionalServiceReviewInformationResponse.builder()
                .title(OptionalServiceReview.getTitle())
                .comment(OptionalServiceReview.getComment())
                .starRating(OptionalServiceReview.getStarRating())
                .customerId(OptionalServiceReview.getCustomerId())
                .optionalServiceId(OptionalServiceReview.getOptionalServiceId())
                .build();
    }
}
