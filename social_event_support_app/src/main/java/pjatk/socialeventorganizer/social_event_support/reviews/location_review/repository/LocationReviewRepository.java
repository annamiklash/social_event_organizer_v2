package pjatk.socialeventorganizer.social_event_support.reviews.location_review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.LocationReview;

import java.util.List;

@Repository
public interface LocationReviewRepository extends JpaRepository<LocationReview, Long> {

    boolean existsLocationReviewByLocation_Id(long id);

    @Query("SELECT lr from location_review lr " +
            "left join fetch lr.customer c " +
            "left join fetch lr.location l WHERE l.id = :id")
    List<LocationReview> getByLocationId(@Param("id") long id);

}
