package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.controller;


import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.OptionalServiceReview;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.dto.ServiceReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.service.OptionalServiceReviewService;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.service.ServiceReviewService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/reviews/service")
public class OptionalServiceReviewController {

    private final ServiceReviewService optionalServiceReviewService;


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceReviewDto> reviewService(@RequestParam long id,
                                                          @RequestParam long serviceId,
                                                          @Valid @RequestBody ServiceReviewDto dto) {

        final OptionalServiceReview review = optionalServiceReviewService.leaveServiceReview(id, serviceId, dto);
        return ResponseEntity.ok(ReviewMapper.toServiceReviewDto(review));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<ServiceReviewDto>> listAllByServiceId(@RequestParam long id) {

        final List<OptionalServiceReview> review = optionalServiceReviewService.getByServiceId(id);
        return ResponseEntity.ok(ImmutableList.copyOf(
                review.stream()
                        .map(ReviewMapper::toServiceReviewDto)
                        .collect(Collectors.toList())
        ));
    }
}
