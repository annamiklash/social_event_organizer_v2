package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.CateringReview;

@Repository
public interface CateringReviewRepository extends JpaRepository<CateringReview, Long> {
}
