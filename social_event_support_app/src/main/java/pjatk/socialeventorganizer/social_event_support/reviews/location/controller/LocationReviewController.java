package pjatk.socialeventorganizer.social_event_support.reviews.location.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.reviews.ReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.location.model.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.location.service.LocationReviewService;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;
import pjatk.socialeventorganizer.social_event_support.table.TableDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/reviews/location")
public class LocationReviewController {

    private final LocationReviewService locationReviewService;

    @RequestMapping(
            path = "allowed/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<ReviewDto>> listAllByLocationId(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                   @RequestParam(defaultValue = "50") Integer pageSize,
                                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                                   @RequestParam(defaultValue = "asc") String order,
                                                                   @RequestParam long locationId) {
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build();
        final List<LocationReview> locationReviewList = locationReviewService.getByLocationId(customPage, locationId);

        final Long count = locationReviewService.count(locationId);

        final ImmutableList<ReviewDto> result = locationReviewList.stream()
                .map(ReviewMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(new TableDto<>(TableDto.MetaDto.builder().pageNo(pageNo).pageSize(pageSize).sortBy(sortBy).total(count).build(), result));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDto> reviewLocation(@RequestParam long customerId,
                                                    @RequestParam long locationId,
                                                    @Valid @RequestBody ReviewDto dto) {

        final LocationReview review = locationReviewService.leaveLocationReview(customerId, locationId, dto);
        return ResponseEntity.ok(ReviewMapper.toDto(review));
    }

}
