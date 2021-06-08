package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.request.CateringReviewRequest;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.response.CateringReviewInformationResponse;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.service.CateringReviewService;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/catering_review")
public class CateringReviewController {

    private final CateringReviewService service;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringReviewInformationResponse>> findAAll() {
        log.info("GET ALL REVIEWS");
        return ResponseEntity.ok(service.findAll());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCateringReview(@RequestBody @Valid CateringReviewRequest request) {
        log.info("ADD REVIEW");
        service.addNewCateringReview(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/id_catering/{id_catering}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringReviewInformationResponse>> getCateringReviewsByCateringId(@PathVariable Long id_catering) {
        log.info("GET " + id_catering);
        return ResponseEntity.ok(service.getCateringReviewByCateringId(id_catering));
    }
}
