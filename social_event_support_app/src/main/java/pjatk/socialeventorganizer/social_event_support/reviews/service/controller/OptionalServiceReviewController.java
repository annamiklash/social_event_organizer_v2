package pjatk.socialeventorganizer.social_event_support.reviews.service.controller;


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
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;
import pjatk.socialeventorganizer.social_event_support.reviews.service.model.OptionalServiceReview;
import pjatk.socialeventorganizer.social_event_support.reviews.service.service.OptionalServiceReviewService;
import pjatk.socialeventorganizer.social_event_support.table.TableDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/reviews/service")
public class OptionalServiceReviewController {

    private final OptionalServiceReviewService optionalServiceReviewService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDto> reviewService(@RequestParam long customerId,
                                                          @RequestParam long serviceId,
                                                          @Valid @RequestBody ReviewDto dto) {

        final OptionalServiceReview review = optionalServiceReviewService.leaveServiceReview(customerId, serviceId, dto);
        return ResponseEntity.ok(ReviewMapper.toDto(review));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<ReviewDto>> listAllByServiceId(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                         @RequestParam(defaultValue = "5") Integer pageSize,
                                                                         @RequestParam(defaultValue = "id") String sortBy,
                                                                         @RequestParam(defaultValue = "asc") String order,
                                                                         @RequestParam long serviceId) {
        final List<OptionalServiceReview> review = optionalServiceReviewService.getByServiceId(CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .order(order)
                .sortBy(sortBy).build(), serviceId);

        final Long count = optionalServiceReviewService.count(serviceId);

        final ImmutableList<ReviewDto> result = ImmutableList.copyOf(
                review.stream()
                        .map(ReviewMapper::toDtoWithCustomerAvatar)
                        .collect(Collectors.toList()));

        return ResponseEntity.ok(new TableDto<>(TableDto.MetaDto.builder().pageNo(pageNo).pageSize(pageSize).sortBy(sortBy).total(count).build(), result));
    }
}
