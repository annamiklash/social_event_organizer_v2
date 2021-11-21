package pjatk.socialeventorganizer.social_event_support.availability.catering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.availability.catering.model.CateringAvailability;

import java.util.List;
import java.util.Optional;

@Repository
public interface CateringAvailabilityRepository extends JpaRepository<CateringAvailability, Long> {

    @Query(value = "select distinct ca.* from catering_availability ca " +
            "where ca.id_catering=:id AND ca.status = 'AVAILABLE' " +
            "AND ca.date = CAST(:date as timestamp)", nativeQuery = true)
    Optional<List<CateringAvailability>> findAvailabilitiesByCateringIdAndDate(@Param("id") long id, @Param("date") String date);
}
