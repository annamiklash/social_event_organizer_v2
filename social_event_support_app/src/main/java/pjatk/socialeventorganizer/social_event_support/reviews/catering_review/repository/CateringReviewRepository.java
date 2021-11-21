package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.CateringReview;

import java.util.List;

@Repository
public interface CateringReviewRepository extends JpaRepository<CateringReview, Long> {

//    @Query("SELECT cr FROM catering_review AS cr WHERE LOWER(cr.title) LIKE %:keyword% OR LOWER(cr.comment) LIKE %:keyword%")
//    Page<CateringReview> findAllWithKeyword(Pageable paging, @Param("keyword") String keyword);
//
    @Query("SELECT cr FROM catering_review AS cr where cr.catering.id = :cateringId")
    Page<CateringReview> findCateringReviewByCateringId(Pageable paging, @Param("cateringId") Long cateringId);



    boolean existsCateringReviewByCatering_Id(long id);

    @Query("SELECT cr from catering_review cr " +
            "left join fetch cr.customer c" +
            "left join fetch cr.catering crr WHERE crr.id = :id")
    List<CateringReview> getByCateringId(@Param("id") long id);

}
