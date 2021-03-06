package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;

import java.util.List;
import java.util.Optional;

@Repository
public interface CateringForLocationRepository extends JpaRepository<CateringForChosenEventLocation, Long> {

    @Query("SELECT c FROM catering_for_chosen_location c " +
            "LEFT JOIN FETCH c.eventLocation cl " +
            "LEFT JOIN FETCH cl.event e " +
            "LEFT JOIN FETCH c.catering cat " +
            "WHERE e.id = :eventId AND cat.id = :cateringId")
    Optional<CateringForChosenEventLocation> findByCateringIdAndEventId(@Param("cateringId") long cateringId, @Param("eventId") Long eventId);

    @Query("SELECT c FROM catering_for_chosen_location c " +
            "LEFT JOIN FETCH c.eventLocation cl " +
            "LEFT JOIN FETCH cl.event e " +
            "LEFT JOIN FETCH e.eventType et " +
            "LEFT JOIN FETCH e.customer ec " +
            "LEFT JOIN FETCH c.catering cat " +
            "WHERE cat.id = :cateringId AND c.confirmationStatus = :status")
    List<CateringForChosenEventLocation> findAllByCateringIdAndStatus(@Param("cateringId") long cateringId, @Param("status") String status);

}
