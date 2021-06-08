package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.dto.OptionalServiceReview;

import java.util.List;


@Repository
public interface OptionalServiceReviewRepository extends JpaRepository<OptionalServiceReview, Long> {

    @Query(value = "select * from service_review where id_optional_service = :optionalServiceId", nativeQuery = true)
    List<OptionalServiceReview> findOptionalServiceReviewByOptionalServiceId(@Param("optionalServiceId") Long optionalServiceId);


}
