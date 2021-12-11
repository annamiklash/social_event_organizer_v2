package pjatk.socialeventorganizer.social_event_support.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.image.model.CateringImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface CateringImageRepository extends JpaRepository<CateringImage, Long> {

    List<CateringImage> findAllByCatering_Id(long locationId);

    @Query("SELECT i from catering_image i " +
            "LEFT JOIN FETCH i.catering c " +
            "WHERE c.id = :cateringId AND i.isMain = TRUE")
    Optional<CateringImage> getMain(@Param("cateringId") long cateringId);

    @Query("SELECT count(i) from catering_image i " +
            "LEFT JOIN FETCH i.catering c " +
            "WHERE c.id = :cateringId")
    int countAll(@Param("serviceId") long serviceId);
}
