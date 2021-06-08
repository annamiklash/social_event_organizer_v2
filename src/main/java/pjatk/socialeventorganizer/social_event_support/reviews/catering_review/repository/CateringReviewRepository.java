package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.dto.CateringReview;

import java.util.List;


@Repository
public interface CateringReviewRepository extends JpaRepository<CateringReview, Long> {

    //List<CateringReview> findCateringReviewByCateringId(Long CateringId);

    @Query(value = "select * from catering_review where id_catering = :cateringId", nativeQuery = true)
    List<CateringReview> findCateringReviewByCateringId(@Param("cateringId") Long cateringId);


}
