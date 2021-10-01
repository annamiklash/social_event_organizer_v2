package pjatk.socialeventorganizer.social_event_support.location.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.location.model.request.LocationDescriptionForFilteringLocationsRequest;
import pjatk.socialeventorganizer.social_event_support.location.model.request.LocationRequest;
import pjatk.socialeventorganizer.social_event_support.location.model.response.LocationInformationResponse;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/location")
public class LocationController {

    private final LocationService locationService;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationInformationResponse>> findAAll() {
        log.info("GET LOCATIONS");
        return ResponseEntity.ok(locationService.findAll());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addLocation(@Valid @RequestBody LocationRequest request) {
        locationService.addNewLocation(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/city/{city}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationInformationResponse>> findByCity(@PathVariable String city) {
        log.info("GET " + city);
        return ResponseEntity.ok(locationService.findByCity(city));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/name/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationInformationResponse>> findByCateringName(@PathVariable String name) {
        log.info("GET " + name);
        return ResponseEntity.ok(locationService.findByName(name));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/description",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationInformationResponse>> filterLocations(@Valid @RequestBody LocationDescriptionForFilteringLocationsRequest request) {
        log.info("GET LOCATIONS BY DESCRIPTION");
        return ResponseEntity.ok(locationService.findByLocationDescription(request));
    }


}
