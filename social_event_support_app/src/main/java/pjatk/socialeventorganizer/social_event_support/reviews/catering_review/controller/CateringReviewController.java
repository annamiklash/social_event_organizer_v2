package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.controller;

import com.google.common.collect.ImmutableList;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.dto.CateringReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.service.CateringReviewService;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;
import pjatk.socialeventorganizer.social_event_support.table.TableDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

public class CateringReviewController {

    private CateringReviewService cateringReviewService;

    @PreAuthorize("hasAnyAuthority('BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<CateringReviewDto>> listAllByCateringId(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                           @RequestParam(defaultValue = "5") Integer pageSize,
                                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                                           @RequestParam long id) {
        final List<CateringReview> review = cateringReviewService.getByCateringId(CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy).build(), id);

        final Long count = cateringReviewService.count(id);

        final ImmutableList<CateringReviewDto> result = ImmutableList.copyOf(
                review.stream()
                        .map(ReviewMapper::toCateringReviewDto)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(new TableDto<>(TableDto.MetaDto.builder().pageNo(pageNo).pageSize(pageSize).sortBy(sortBy).total(count).build(), result));
    }

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


}
