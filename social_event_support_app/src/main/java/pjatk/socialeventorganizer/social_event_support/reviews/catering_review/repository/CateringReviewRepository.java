package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.CateringReview;

import java.util.List;

@Repository
public interface CateringReviewRepository extends JpaRepository<CateringReview, Long> {

    boolean existsCateringReviewByCatering_Id(long id);

    @Query("SELECT cr from catering_review cr " +
            "left join fetch cr.customer c" +
            "left join fetch cr.catering crr WHERE crr.id = :id")
    List<CateringReview> getByCateringId(@Param("id") long id);
}
