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
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.dto.CateringReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.service.CateringReviewService;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;


import com.google.common.collect.ImmutableList;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.dto.CateringReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.service.CateringReviewService;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

public class CateringReviewController {

    private CateringReviewService cateringReviewService;

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
            path = "by_catering",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringReviewDto>> listByCatering(@RequestParam(required = false) Long id_catering,
                                                                           @RequestParam(defaultValue = "0") Integer firstResult,
                                                                           @RequestParam(defaultValue = "6") Integer maxResult,
                                                                           @RequestParam(defaultValue = "id") String sort,
                                                                           @RequestParam(defaultValue = "desc") String order) {
        log.info("GET ALL CATERINGREVIEWS");

        final ImmutableList<CateringReview> list = service.listByCatering(new CustomPage(maxResult, firstResult, sort, order), id_catering);

        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(ReviewMapper::toCateringReviewDto)
                        .collect(Collectors.toList())));
                                                                           }
    @PreAuthorize("hasAnyAuthority('BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
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
