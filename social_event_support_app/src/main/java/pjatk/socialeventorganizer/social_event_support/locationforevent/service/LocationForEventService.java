package pjatk.socialeventorganizer.social_event_support.locationforevent.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.locationforevent.repository.LocationForEventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static pjatk.socialeventorganizer.social_event_support.locationforevent.enums.ConfirmationStatusEnum.CONFIRMED;

@Service
@AllArgsConstructor
@Slf4j
public class LocationForEventService {

    private final LocationForEventRepository locationForEventRepository;

    private final OrganizedEventService organizedEventService;

    public List<LocationForEvent> getLocationInfoByOrganizedEventId(long eventId) {
        final Optional<List<LocationForEvent>> optionalLocationForEvent = locationForEventRepository.findLocationForEventByOrganizedEventId(eventId);
        if (optionalLocationForEvent.isPresent()) {
            return optionalLocationForEvent.get();
        }
        throw new NotFoundException("Event with id " + eventId + " does not exist");

    }

    public LocationForEvent confirmReservation(long id, long eventId) {
        final LocationForEvent locationForEvent = findByLocationIdAndEventId(id, eventId);

        locationForEvent.setConfirmationStatus(CONFIRMED.toString());
        save(locationForEvent);

        final OrganizedEvent organizedEvent = locationForEvent.getEvent();

        organizedEvent.setModifiedAt(LocalDateTime.now());
        organizedEventService.save(organizedEvent);

        return locationForEvent;
    }

    public List<LocationForEvent> listAllByStatus(long locationId, String status) {

        final Optional<List<LocationForEvent>> optionalList = locationForEventRepository.findAllByLocationIdAndStatus(locationId, status);

        if (optionalList.isPresent()) {
            return optionalList.get();
        }
        throw new NotFoundException("There are no requests");
    }

    public void save(LocationForEvent locationForEvent) {
        locationForEventRepository.save(locationForEvent);
    }

    public LocationForEvent findByLocationIdAndEventId(long id, long eventId) {

        final Optional<LocationForEvent> optionalLocationForEvent = locationForEventRepository.findByEventIdAndLocationId(eventId, id);

        if (optionalLocationForEvent.isPresent()) {
            return optionalLocationForEvent.get();
        }
        throw new NotFoundException("Location for event does not exist");
    }

}
