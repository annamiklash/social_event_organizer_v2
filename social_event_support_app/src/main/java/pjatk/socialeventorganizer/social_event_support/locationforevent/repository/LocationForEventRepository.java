package pjatk.socialeventorganizer.social_event_support.locationforevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.LocationForEvent;

import java.util.List;

@Repository
public interface LocationForEventRepository extends JpaRepository<LocationForEvent, Long> {

    @Query("select lfe " +
            "from location_for_event lfe " +
            "join organized_event oe on oe.id = lfe.event.id " +
            "join location l on l.id = lfe.location.id " +
            "where oe.id=:id")
    List<LocationForEvent> findLocationForEventByOrganizedEventId(@Param("id") Long id);
}
