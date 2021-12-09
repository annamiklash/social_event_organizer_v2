package pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;

import java.util.List;

@Repository
public interface OptionalServiceAvailabilityRepository extends JpaRepository<OptionalServiceAvailability, Long> {

    @Query(value = "select distinct os.* from optional_service_availability os " +
            "where os.id_optional_service=:id " +
            "AND os.date = CAST(:date as timestamp)", nativeQuery = true)
    List<OptionalServiceAvailability> findAvailabilitiesByServiceIdAndDate(@Param("id") long id, @Param("date") String date);

    @Query(value = "select distinct sa.* from optional_service_availability sa " +
            "where sa.id_optional_service=:id AND sa.status = 'AVAILABLE' " +
            "AND sa.date = CAST(:date as timestamp)", nativeQuery = true)
    List<OptionalServiceAvailability> findByDate(@Param("id") Long id, @Param("date") String date);
}
