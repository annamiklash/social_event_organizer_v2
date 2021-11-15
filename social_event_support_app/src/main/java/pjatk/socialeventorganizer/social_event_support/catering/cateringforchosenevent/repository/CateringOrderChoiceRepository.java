package pjatk.socialeventorganizer.social_event_support.catering.cateringforchosenevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.catering.cateringforchosenevent.model.CateringOrderChoice;

@Repository
public interface CateringOrderChoiceRepository extends JpaRepository<CateringOrderChoice, Long> {
}
