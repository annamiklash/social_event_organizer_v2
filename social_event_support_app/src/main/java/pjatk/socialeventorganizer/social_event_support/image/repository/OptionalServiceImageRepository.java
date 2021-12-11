package pjatk.socialeventorganizer.social_event_support.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.image.model.OptionalServiceImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionalServiceImageRepository extends JpaRepository<OptionalServiceImage, Long> {

    List<OptionalServiceImage> findAllByService_Id(long locationId);

    @Query("SELECT i from optional_service_image i " +
            "LEFT JOIN FETCH i.service s " +
            "WHERE s.id = :serviceId AND i.isMain = TRUE")
    Optional<OptionalServiceImage> getMain(@Param("serviceId") long serviceId);

    @Query("SELECT count(i) from optional_service_image i " +
            "LEFT JOIN FETCH i.service s " +
            "WHERE s.id = :serviceId")
    int countAll(@Param("serviceId") long serviceId);
}
