package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.OptionalServiceReview;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.repository.ServiceReviewRepository;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceReviewService {

    private final ServiceReviewRepository serviceReviewRepository;

    public void save(OptionalServiceReview optionalServiceReview) {
        serviceReviewRepository.save(optionalServiceReview);
    }
}
