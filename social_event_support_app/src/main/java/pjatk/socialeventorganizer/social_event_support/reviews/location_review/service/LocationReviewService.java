package pjatk.socialeventorganizer.social_event_support.reviews.location_review.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.repository.LocationReviewRepository;

@Service
@AllArgsConstructor
@Slf4j
public class LocationReviewService {

    private LocationReviewRepository locationReviewRepository;

    public void save(LocationReview locationReview) {
        locationReviewRepository.save(locationReview);
    }
}
