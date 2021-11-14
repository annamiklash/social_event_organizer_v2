package pjatk.socialeventorganizer.social_event_support.locationforevent.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.locationforevent.mapper.LocationForEventMapper;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.dto.LocationForEventDto;
import pjatk.socialeventorganizer.social_event_support.locationforevent.service.LocationForEventService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/event/location")
public class LocationForEventController {

    private final LocationForEventService locationForEventService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "confirm",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationForEventDto> confirmReservation(@RequestParam long id, @RequestParam long eventId) {

        final LocationForEvent locationForEvent = locationForEventService.confirmReservation(id, eventId);

        return ResponseEntity.ok(LocationForEventMapper.toDto(locationForEvent));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationForEventDto>> listAllUnconfirmed(@RequestParam String status, @RequestParam long locationId) {

        List<LocationForEvent> locationsForEvent = locationForEventService.listAllByStatus(locationId, status);

        return ResponseEntity.ok(
                ImmutableList.copyOf(locationsForEvent.stream()
                        .map(LocationForEventMapper::toDtoWithLocationAndEvent)
                        .collect(Collectors.toList())));

    }

}
