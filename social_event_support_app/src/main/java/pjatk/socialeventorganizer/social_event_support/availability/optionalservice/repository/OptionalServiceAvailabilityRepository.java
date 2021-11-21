package pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionalServiceAvailabilityRepository extends JpaRepository<OptionalServiceAvailability, Long> {

    @Query(value = "select distinct os.* from optional_service_availability os " +
            "where os.id_optional_service=:id AND os.status = 'AVAILABLE' " +
            "AND os.date = CAST(:date as timestamp)", nativeQuery = true)
    Optional<List<OptionalServiceAvailability>> findAvailabilitiesByServiceIdAndDate(@Param("id") long id, @Param("date") String date);
}
