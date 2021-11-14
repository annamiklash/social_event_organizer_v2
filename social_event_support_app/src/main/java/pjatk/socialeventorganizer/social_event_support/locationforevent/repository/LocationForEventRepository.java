package pjatk.socialeventorganizer.social_event_support.locationforevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.LocationForEvent;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationForEventRepository extends JpaRepository<LocationForEvent, Long> {

    @Query("select lfe " +
            "from location_for_event lfe " +
            "join organized_event oe on oe.id = lfe.event.id " +
            "join location l on l.id = lfe.location.id " +
            "where oe.id=:id")
    Optional<List<LocationForEvent>> findLocationForEventByOrganizedEventId(@Param("id") Long id);

    @Query("select lfe " +
            "from location_for_event lfe " +
            "left join fetch lfe.event lfee " +
            "left join fetch location l on l.id = lfe.location.id " +
            "where l.id = :locationId AND lfee.id = :eventId")
    Optional<LocationForEvent> findByEventIdAndLocationId(@Param("eventId") long eventId, @Param("locationId") long locationId);


    @Query("SELECT lfe from location_for_event lfe " +
            "left join fetch lfe.event e " +
            "left join fetch e.customer ec " +
            "left join fetch lfe.location l " +
            "left join fetch e.eventType et " +
            "where l.id = :locationId AND lfe.confirmationStatus = :status")
    Optional<List<LocationForEvent>> findAllByLocationIdAndStatus(@Param("locationId") long locationId, @Param("status") String status);
}
