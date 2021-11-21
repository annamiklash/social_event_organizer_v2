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

    private ServiceReviewService serviceReviewService;


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceReviewDto> reviewService(@RequestParam long id,
                                                          @RequestParam long serviceId,
                                                          @Valid @RequestBody ServiceReviewDto dto) {

        final OptionalServiceReview review = serviceReviewService.leaveServiceReview(id, serviceId, dto);
        return ResponseEntity.ok(ReviewMapper.toServiceReviewDto(review));
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<ServiceReviewDto>> listAllByServiceId(@RequestParam long id) {

        final List<OptionalServiceReview> review = serviceReviewService.getByServiceId(id);
        return ResponseEntity.ok(ImmutableList.copyOf(
                review.stream()
                        .map(ReviewMapper::toServiceReviewDto)
                        .collect(Collectors.toList())
        ));
    }
}
