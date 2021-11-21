package pjatk.socialeventorganizer.social_event_support.businesshours.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.businesshours.location.model.LocationBusinessHours;

@Repository
public interface LocationBusinessHoursRepository extends JpaRepository<LocationBusinessHours, Long> {
}
