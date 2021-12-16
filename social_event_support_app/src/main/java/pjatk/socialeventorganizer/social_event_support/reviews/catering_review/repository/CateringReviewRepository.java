package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            "left join customer c on c.id = cr.customer.id " +
            "left join catering cat on cat.id = cr.catering.id WHERE cat.id = :id")
    List<CateringReview> getByCateringId(@Param("id") long id);

    Long countAllByCatering_Id(long id);

    @Query("SELECT count(cr) from catering_review cr " +
            "left join customer c on c.id = cr.customer.id " +
            "left join catering cat on cat.id = cr.catering.id WHERE cat.id = :id")
    Page<CateringReview> getByCateringId(long id, Pageable pageable);
}
