package pjatk.socialeventorganizer.social_event_support.event.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.event.dto.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.repository.OrganizedEventRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OrganizedEventService {

    private final OrganizedEventRepository organizedEventRepository;

    public ImmutableList<OrganizedEvent> findAll() {
        final List<OrganizedEvent> all = organizedEventRepository.findAll();
        return ImmutableList.copyOf(all);

    }

    public OrganizedEvent getByOrganizedEventId(long orgEventId) {
        final Optional<OrganizedEvent> optionalEvent = organizedEventRepository.findById(orgEventId);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        }
        throw new NotFoundException("No organized event with id " + orgEventId);
    }
}
