package pjatk.socialeventorganizer.social_event_support.reviews.location_review.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.dto.LocationReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.service.LocationReviewService;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/reviews/location")
public class LocationReviewController {

    private LocationReviewService locationReviewService;

    //TODO: add verification if location/catering/service was booked
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationReviewDto> reviewLocation(@RequestParam long customerId,
                                                            @RequestParam long locationId,
                                                            @Valid @RequestBody LocationReviewDto dto) {

        final LocationReview review = locationReviewService.leaveLocationReview(customerId, locationId, dto);
        return ResponseEntity.ok(ReviewMapper.toLocationReviewDto(review));
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationReviewDto>> listAllByLocationId(@RequestParam long locationId) {

        final List<LocationReview> review = locationReviewService.getByLocationId(locationId);
        return ResponseEntity.ok(ImmutableList.copyOf(
                review.stream()
                        .map(ReviewMapper::toLocationReviewDto)
                        .collect(Collectors.toList())
        ));
    }

}
