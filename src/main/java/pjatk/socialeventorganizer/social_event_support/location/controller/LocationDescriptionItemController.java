package pjatk.socialeventorganizer.social_event_support.location.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDescriptionItem;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationDescriptionItemService;


@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/location_description")
public class LocationDescriptionItemController {

    private final LocationDescriptionItemService service;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationDescriptionItem>> findAAll() {
        log.info("GET LOCATION DESCRIPTIONS");
        return ResponseEntity.ok(service.findAll());
    }
}
