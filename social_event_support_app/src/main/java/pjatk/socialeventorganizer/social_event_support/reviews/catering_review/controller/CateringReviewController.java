package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.dto.CateringReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.service.CateringReviewService;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/reviews/catering")
public class CateringReviewController {

    private final CateringReviewService cateringReviewService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringReviewDto> reviewCatering(@RequestParam long id,
                                                            @RequestParam long cateringId,
                                                            @Valid @RequestBody CateringReviewDto dto) {

        final CateringReview review = cateringReviewService.leaveCateringReview(id, cateringId, dto);
        return ResponseEntity.ok(ReviewMapper.toCateringReviewDto(review));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringReviewDto>> listAllByCateringId(@RequestParam long id) {

        final List<CateringReview> review = cateringReviewService.getByCateringId(id);
        return ResponseEntity.ok(ImmutableList.copyOf(
                review.stream()
                        .map(ReviewMapper::toCateringReviewDto)
                        .collect(Collectors.toList())
        ));
    }
}
