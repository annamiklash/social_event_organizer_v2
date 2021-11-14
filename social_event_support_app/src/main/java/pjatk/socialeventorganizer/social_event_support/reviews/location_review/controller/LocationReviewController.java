package pjatk.socialeventorganizer.social_event_support.reviews.location_review.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.service.LocationReviewService;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/reviews/location")
public class LocationReviewController {

    private LocationReviewService locationReviewService;

}
