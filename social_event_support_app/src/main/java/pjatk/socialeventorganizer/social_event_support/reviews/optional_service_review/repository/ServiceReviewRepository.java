package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.OptionalServiceReview;

import java.util.List;

@Repository
public interface ServiceReviewRepository extends JpaRepository<OptionalServiceReview, Long> {

    boolean existsByOptionalService_Id(long id);

    @Query("SELECT sr from service_review sr " +
            "left join fetch sr.customer c " +
            "left join fetch sr.optionalService os WHERE os.id = :id")
    List<OptionalServiceReview> getByServiceId(@Param("id") long id);

    @Query("SELECT sr from service_review sr " +
            "left join customer c on c.id = sr.customer.id " +
            "left join optional_service os on os.id = sr.optionalService.id WHERE os.id = :id")
    Page<OptionalServiceReview> getByServiceId(@Param("id") long id, Pageable pageable);

    long countAllByOptionalService_Id(long id);


}
