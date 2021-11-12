package pjatk.socialeventorganizer.social_event_support.locationforevent.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.invite.mapper.LocationInfoMapper;
import pjatk.socialeventorganizer.social_event_support.invite.response.LocationInfoResponse;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.locationforevent.repository.LocationForEventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class LocationForEventService {

    private final LocationForEventRepository locationForEventRepository;

    private final LocationInfoMapper locationInfoMapper;

    public List<LocationInfoResponse> getLocationInfoByOrganizedEventId(long eventId) {
        final List<LocationForEvent> locationsForEvent = locationForEventRepository.findLocationForEventByOrganizedEventId(eventId);
        return locationsForEvent.stream()
                .map(locationInfoMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
