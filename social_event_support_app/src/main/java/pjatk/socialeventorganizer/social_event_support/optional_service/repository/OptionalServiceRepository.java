package pjatk.socialeventorganizer.social_event_support.optional_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;

@Repository
public interface OptionalServiceRepository extends JpaRepository<OptionalService, Long> {

    @Query("SELECT os from optional_service os " +
            "WHERE (:keyword is null or os.type LIKE %:keyword%) " +
            "OR (:keyword is null or os.description LIKE %:keyword%) " +
            "OR (:keyword is null or os.alias LIKE %:keyword%)")
    Page<OptionalService> findAllWithKeyword(Pageable paging, @Param("keyword") String keyword);

}
