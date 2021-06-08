package pjatk.socialeventorganizer.social_event_support.reviews.location_review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.dto.LocationReview;

import java.util.List;


@Repository
public interface LocationReviewRepository extends JpaRepository<LocationReview, Long> {

    @Query(value = "select * from location_review where id_location = :locationId", nativeQuery = true)
    List<LocationReview> findLocationReviewByLocationId(@Param("locationId") Long locationId);


}
