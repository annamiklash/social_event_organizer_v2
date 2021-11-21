package pjatk.socialeventorganizer.social_event_support.businesshours.catering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours;

@Repository
public interface CateringBusinessHoursRepository extends JpaRepository<CateringBusinessHours, Long> {
}
