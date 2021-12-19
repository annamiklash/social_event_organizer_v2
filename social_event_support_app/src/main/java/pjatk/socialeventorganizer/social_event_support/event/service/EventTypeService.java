package pjatk.socialeventorganizer.social_event_support.event.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.event.model.EventType;
import pjatk.socialeventorganizer.social_event_support.event.repository.EventRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EventTypeService {

    private final EventRepository eventRepository;

    public ImmutableList<EventType> findAll() {
        final List<EventType> all = eventRepository.findAll();
        return ImmutableList.copyOf(all);
    }


    public EventType getByType(String type) {
        final Optional<EventType> optionalType = eventRepository.findByType(type);

        if (optionalType.isPresent()) {
            return optionalType.get();
        }

        throw new NotFoundException("Event type " + type + " does not exist");
    }
}
