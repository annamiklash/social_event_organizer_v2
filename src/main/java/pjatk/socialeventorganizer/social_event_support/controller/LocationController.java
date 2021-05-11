package pjatk.socialeventorganizer.social_event_support.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.model.dto.Location;
import pjatk.socialeventorganizer.social_event_support.model.request.LocationDescriptionRequestForFilteringLocations;
import pjatk.socialeventorganizer.social_event_support.model.request.LocationRequest;
import pjatk.socialeventorganizer.social_event_support.model.response.LocationResponse;
import pjatk.socialeventorganizer.social_event_support.service.LocationService;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/location")
public class LocationController {

    private final LocationService service;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<Location>> findAAll() {
        log.info("GET LOCATIONS");
        return ResponseEntity.ok(service.findAll());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationResponse> addLocation(@Valid @RequestBody LocationRequest request) {
        final LocationResponse response = service.addNewLocation(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/description",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<Location>> findAllByDescription(@Valid @RequestBody LocationDescriptionRequestForFilteringLocations request) {
        log.info("GET LOCATIONS BY DESCRIPTION");
        return ResponseEntity.ok(service.findByLocationDescription(request));
    }


}
