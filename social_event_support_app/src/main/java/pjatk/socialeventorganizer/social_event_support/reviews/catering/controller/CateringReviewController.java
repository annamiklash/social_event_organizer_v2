package pjatk.socialeventorganizer.social_event_support.reviews.catering.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.reviews.catering.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering.model.dto.CateringReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.catering.service.CateringReviewService;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;
import pjatk.socialeventorganizer.social_event_support.table.TableDto;

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

    @PreAuthorize("hasAnyAuthority('BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<CateringReviewDto>> listAllByCateringId(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                           @RequestParam(defaultValue = "50") Integer pageSize,
                                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                                           @RequestParam(defaultValue = "asc") String order,
                                                                           @RequestParam long id) {
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build();
        final List<CateringReview> review = cateringReviewService.getByCateringId(customPage, id);

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
