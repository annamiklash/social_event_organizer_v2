package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.request.OptionalServiceReviewRequest;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.response.OptionalServiceReviewInformationResponse;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.service.OptionalServiceReviewService;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/optional_service_review")
public class OptionalServiceReviewController {

    private final OptionalServiceReviewService service;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OptionalServiceReviewInformationResponse>> findAAll() {
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
    public ResponseEntity<Void> createOptionalServiceReview(@RequestBody @Valid OptionalServiceReviewRequest request) {
        log.info("ADD REVIEW");
        service.addNewOptionalServiceReview(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/id_optionalService/{id_optionalService}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OptionalServiceReviewInformationResponse>> getOptionalServiceReviewsByOptionalServiceId(@PathVariable Long id_optionalService) {
        log.info("GET " + id_optionalService);
        return ResponseEntity.ok(service.getOptionalServiceReviewByOptionalServiceId(id_optionalService));
    }
}
