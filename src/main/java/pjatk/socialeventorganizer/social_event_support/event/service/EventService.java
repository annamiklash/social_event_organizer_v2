package pjatk.socialeventorganizer.social_event_support.event.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.event.dto.EventType;
import pjatk.socialeventorganizer.social_event_support.event.repository.EventRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;

    public ImmutableList<EventType> findAll() {
        final List<EventType> all = eventRepository.findAll();
        return ImmutableList.copyOf(all);
    }
}
