package pjatk.socialeventorganizer.social_event_support.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjatk.socialeventorganizer.social_event_support.event.model.EventType;

public interface EventRepository extends JpaRepository<EventType, Long> {

}
