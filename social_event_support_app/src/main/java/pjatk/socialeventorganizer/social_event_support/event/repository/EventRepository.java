package pjatk.socialeventorganizer.social_event_support.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.event.model.EventType;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventType, Long> {

    Optional<EventType> findByType(String name);
}
