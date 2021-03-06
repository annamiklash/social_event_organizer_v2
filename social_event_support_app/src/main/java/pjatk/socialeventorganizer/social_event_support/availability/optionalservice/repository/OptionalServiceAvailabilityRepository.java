package pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.availability.location.model.LocationAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionalServiceAvailabilityRepository extends JpaRepository<OptionalServiceAvailability, Long> {

    @Query(value = "select distinct os.* " +
            "from optional_service_availability os " +
            "where os.id_optional_service=:id " +
            "AND os.date = CAST(:date as timestamp)", nativeQuery = true)
    List<OptionalServiceAvailability> findAvailabilitiesByServiceIdAndDate(@Param("id") long id, @Param("date") String date);

    @Query(value = "select distinct sa.* " +
            "from optional_service_availability sa " +
            "where sa.id_optional_service=:id AND sa.status = 'AVAILABLE' " +
            "AND sa.date = CAST(:date as timestamp)", nativeQuery = true)
    List<OptionalServiceAvailability> findByDate(@Param("id") Long id, @Param("date") String date);

    @Query(value = "select distinct la.* from optional_service_availability la " +
            "where la.id_optional_service=:serviceId AND la.status = 'AVAILABLE' " +
            "AND la.time_to = CAST(:timeTo as timestamp)", nativeQuery = true)
    Optional<OptionalServiceAvailability> findByServiceIdAndTimeTo(@Param("serviceId") Long serviceId, @Param("timeTo") String timeTo);

    @Query(value = "select distinct la.* from optional_service_availability la " +
            "where la.id_optional_service=:serviceId AND la.status = 'AVAILABLE' " +
            "AND la.time_from = CAST(:timeFrom as timestamp)", nativeQuery = true)
    Optional<OptionalServiceAvailability> findByServiceIdAndTimeFrom(@Param("serviceId") Long serviceId, @Param("timeFrom") String timeFrom);
}
