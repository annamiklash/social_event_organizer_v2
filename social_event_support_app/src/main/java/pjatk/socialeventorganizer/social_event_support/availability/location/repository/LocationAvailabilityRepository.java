package pjatk.socialeventorganizer.social_event_support.availability.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.availability.location.model.LocationAvailability;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationAvailabilityRepository extends JpaRepository<LocationAvailability, Long> {

    @Query(value = "select distinct la.* from location_availability la " +
            "where la.id_location=:id AND la.status = 'AVAILABLE' " +
            "AND la.date = CAST(:date as timestamp)", nativeQuery = true)
    Optional<List<LocationAvailability>> findAvailabilitiesByLocationIdAndDate(@Param("id") Long id, @Param("date") String date);
}
