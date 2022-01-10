package pjatk.socialeventorganizer.social_event_support.trait.reviews

import pjatk.socialeventorganizer.social_event_support.reviews.ReviewDto

trait ReviewDtoTrait {

    ReviewDto fakeReviewDto = ReviewDto.builder()
            .id(1L)
            .title('SAMPLE TITLE')
            .comment('SAMPLE COMMENT')
            .starRating(5)
            .build()

}