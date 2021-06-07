package pjatk.socialeventorganizer.social_event_support.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjatk.socialeventorganizer.social_event_support.event.dto.EventType;

public interface EventRepository extends JpaRepository<EventType, Long> {

}
