package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.OptionalServiceReview;

@Repository
public interface ServiceReviewRepository extends JpaRepository<OptionalServiceReview, Long> {
}
