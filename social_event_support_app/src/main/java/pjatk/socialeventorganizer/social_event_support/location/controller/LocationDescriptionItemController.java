package pjatk.socialeventorganizer.social_event_support.location.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.enums.LocationDescriptionItemEnum;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationDescriptionItemService;


@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/location_description")
public class LocationDescriptionItemController {

    private final LocationDescriptionItemService service;

    @RequestMapping(
            path = "allowed/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationDescriptionItemEnum>> list() {
        log.info("GET LOCATION DESCRIPTIONS");
        return ResponseEntity.ok(ImmutableList.copyOf(LocationDescriptionItemEnum.values()));
    }


}
