package pjatk.socialeventorganizer.social_event_support.reviews.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringMapper;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.location.mapper.LocationMapper;
import pjatk.socialeventorganizer.social_event_support.optional_service.mapper.OptionalServiceMapper;
import pjatk.socialeventorganizer.social_event_support.reviews.catering.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering.model.dto.CateringReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.location.model.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.location.model.dto.LocationReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.service.model.OptionalServiceReview;
import pjatk.socialeventorganizer.social_event_support.reviews.service.model.dto.ServiceReviewDto;

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

    public LocationReviewDto toLocationReviewDto(LocationReview locationReview) {
        return LocationReviewDto.builder()
                .id(locationReview.getId())
                .title(locationReview.getTitle())
                .comment(locationReview.getComment())
                .starRating(locationReview.getStarRating())
                .location(LocationMapper.toDto(locationReview.getLocation()))
                .customer(CustomerMapper.toDto(locationReview.getCustomer()))
                .build();
    }

    public CateringReviewDto toCateringReviewDto(CateringReview cateringReview) {
        return CateringReviewDto.builder()
                .id(cateringReview.getId())
                .title(cateringReview.getTitle())
                .comment(cateringReview.getComment())
                .starRating(cateringReview.getStarRating())
                .customer(CustomerMapper.toDto(cateringReview.getCustomer()))
                .catering(CateringMapper.toDto(cateringReview.getCatering()))
                .build();
    }

    public ServiceReviewDto toServiceReviewDto(OptionalServiceReview optionalServiceReview) {
        return ServiceReviewDto.builder()
                .id(optionalServiceReview.getId())
                .title(optionalServiceReview.getTitle())
                .comment(optionalServiceReview.getComment())
                .starRating(optionalServiceReview.getStarRating())
                .customer(CustomerMapper.toDto(optionalServiceReview.getCustomer()))
                .service(OptionalServiceMapper.toDto(optionalServiceReview.getOptionalService()))
                .build();
    }

}
