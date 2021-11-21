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

    @Query("select cfcl " +
            "from organized_event oe " +
            "join location_for_event lfe on lfe.event.id=oe.id " +
            "join catering_for_chosen_location cfcl on lfe.id = cfcl.eventLocation.id " +
            "join catering ca on ca.id = cfcl.catering.id " +
            "where oe.id=:orgEventId and lfe.location.id=:locationId")
    Optional<List<CateringForChosenEventLocation>> findCateringForChosenEventLocationByOrganizedEventId(@Param("orgEventId") Long orgEventId, @Param("locationId") long locationId);
}
