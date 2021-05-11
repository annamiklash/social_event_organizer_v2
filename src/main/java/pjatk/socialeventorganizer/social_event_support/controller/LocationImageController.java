package pjatk.socialeventorganizer.social_event_support.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.model.dto.LocationImage;
import pjatk.socialeventorganizer.social_event_support.model.exception.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.model.request.LocationImageRequest;
import pjatk.socialeventorganizer.social_event_support.model.response.ImageResponse;
import pjatk.socialeventorganizer.social_event_support.service.LocationImageService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/location_image")
public class LocationImageController {

    private final LocationImageService service;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageResponse> addLocation(@Valid @RequestBody LocationImageRequest request) {
        final ImageResponse response = service.addImagesToLocation(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/location/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LocationImage>> getImagesByLocationId(@PathVariable Integer id) {
        try {
            final List<LocationImage> response = service.findByLocationId(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
}
