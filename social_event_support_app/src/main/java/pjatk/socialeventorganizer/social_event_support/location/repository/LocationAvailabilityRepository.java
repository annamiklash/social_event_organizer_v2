package pjatk.socialeventorganizer.social_event_support.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjatk.socialeventorganizer.social_event_support.location.model.LocationAvailability;

public interface LocationAvailabilityRepository extends JpaRepository<LocationAvailability, Long> {
}
