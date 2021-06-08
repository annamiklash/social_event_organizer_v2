package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.dto.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.request.CateringReviewRequest;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.response.CateringReviewInformationResponse;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.response.CateringReviewResponse;

@Component
public class CateringReviewMapper {

    public CateringReview mapToDTO(CateringReviewRequest request, Integer costumerId) {
        return CateringReview.builder()
                .title(request.getTitle())
                .comment(request.getComment())
                .cateringId(request.getCateringId())
                .starRating(request.getStarRating())
                .customerId(costumerId)
                .build();
    }

    public CateringReviewResponse mapToResponse(CateringReview cateringReview) {
        return CateringReviewResponse.builder()
                .id(cateringReview.getId())
                .title(cateringReview.getTitle())
                .comment(cateringReview.getComment())
                .build();
    }

    public CateringReviewInformationResponse mapDTOtoCateringReviewInformationResponse(CateringReview CateringReview) {
        return CateringReviewInformationResponse.builder()
                .title(CateringReview.getTitle())
                .comment(CateringReview.getComment())
                .starRating(CateringReview.getStarRating())
                .customerId(CateringReview.getCustomerId())
                .cateringId(CateringReview.getCateringId())
                .build();
    }
}
