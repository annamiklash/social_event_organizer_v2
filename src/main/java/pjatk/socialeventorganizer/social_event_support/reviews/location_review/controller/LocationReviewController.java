package pjatk.socialeventorganizer.social_event_support.reviews.location_review.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.request.LocationReviewRequest;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.response.LocationReviewInformationResponse;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.service.LocationReviewService;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/location_review")
public class LocationReviewController {

    private final LocationReviewService service;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationReviewInformationResponse>> findAAll() {
        log.info("GET ALL REVIEWS");
        return ResponseEntity.ok(service.findAll());
    }
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/id/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findById(@PathVariable Long id) {
        log.info("GET " + id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createLocationReview(@RequestBody @Valid LocationReviewRequest request) {
        log.info("ADD REVIEW");
        service.addNewLocationReview(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/id_location/{id_location}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationReviewInformationResponse>> getLocationReviewsByLocationId(@PathVariable Long id_location) {
        log.info("GET " + id_location);
        return ResponseEntity.ok(service.getLocationReviewByLocationId(id_location));
    }
}
