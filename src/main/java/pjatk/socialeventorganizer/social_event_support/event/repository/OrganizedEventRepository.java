package pjatk.socialeventorganizer.social_event_support.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.event.dto.OrganizedEvent;

@Repository
public interface OrganizedEventRepository extends JpaRepository<OrganizedEvent, Long> {
}
