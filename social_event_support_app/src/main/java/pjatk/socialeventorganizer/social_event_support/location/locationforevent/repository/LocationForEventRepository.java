package pjatk.socialeventorganizer.social_event_support.location.locationforevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;

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
    List<LocationForEvent> findAllByLocationIdAndStatus(@Param("locationId") long locationId, @Param("status") String status);

    @Query("SELECT lfe from location_for_event lfe " +
            "LEFT JOIN FETCH lfe.location l " +
            "LEFT JOIN FETCH l.availability " +
            "WHERE lfe.id = :locationForEventId")
    Optional<LocationForEvent> findByIdWithLocation(@Param("locationForEventId")long locationForEventId);

    @Query("SELECT lfe from location_for_event lfe " +
            "LEFT JOIN FETCH lfe.location l " +
            "LEFT JOIN FETCH lfe.cateringsForEventLocation c " +
            "LEFT JOIN FETCH lfe.event e " +
            "LEFT JOIN FETCH lfe.services s " +
            "LEFT JOIN FETCH l.availability " +
            "WHERE lfe.id = :locationForEventId")
    Optional<LocationForEvent> getWithLocationAndEvent(@Param("locationForEventId")long locationForEventId);

}
