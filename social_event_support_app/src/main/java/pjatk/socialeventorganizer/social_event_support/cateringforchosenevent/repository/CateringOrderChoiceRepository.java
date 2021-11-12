package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.dto.CateringOrderChoice;

@Repository
public interface CateringOrderChoiceRepository extends JpaRepository<CateringOrderChoice, Long> {
}
