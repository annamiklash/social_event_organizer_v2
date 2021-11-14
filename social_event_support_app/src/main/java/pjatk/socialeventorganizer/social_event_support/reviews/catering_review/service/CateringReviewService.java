package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.repository.CateringReviewRepository;

@Service
@AllArgsConstructor
@Slf4j
public class CateringReviewService {

    private final CateringReviewRepository cateringReviewRepository;

    public void save(CateringReview cateringReview) {
        cateringReviewRepository.save(cateringReview);
    }
}
