package pjatk.socialeventorganizer.social_event_support.reviews.location_review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.LocationReview;

@Repository
public interface LocationReviewRepository extends JpaRepository<LocationReview, Long> {
}
