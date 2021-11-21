package pjatk.socialeventorganizer.social_event_support.businesshours.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.model.OptionalServiceBusinessHours;

@Repository
public interface OptionalServiceBusinessHoursRepository extends JpaRepository<OptionalServiceBusinessHours, Long> {
}
