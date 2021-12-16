package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.OptionalServiceReview;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.dto.ServiceReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.repository.ServiceReviewRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceReviewService {

    private final ServiceReviewRepository serviceReviewRepository;

    private final CustomerService customerService;

    private final OptionalServiceService optionalServiceService;

    public void save(OptionalServiceReview optionalServiceReview) {
        serviceReviewRepository.save(optionalServiceReview);
    }

    public OptionalServiceReview leaveServiceReview(long id, long serviceId, ServiceReviewDto dto) {
        final Customer customer = customerService.get(id);

        final OptionalService optionalService = optionalServiceService.get(serviceId);

        final OptionalServiceReview optionalServiceReview = ReviewMapper.fromServiceReviewDto(dto);
        optionalServiceReview.setOptionalService(optionalService);
        optionalServiceReview.setCustomer(customer);
        optionalServiceReview.setCreatedAt(LocalDateTime.now());

        save(optionalServiceReview);

        return optionalServiceReview;
    }

    public List<OptionalServiceReview> getByServiceId(CustomPage paging, long id) {
        if (!exists(id)) {
            throw new NotFoundException("Catering with id " + id + " does not exist");
        }
        final Pageable pageable = PageRequest.of(paging.getPageNo(), paging.getPageSize(),
                Sort.by(paging.getSortBy()));
        final Page<OptionalServiceReview> page = serviceReviewRepository.getByServiceId(id, pageable);

        return ImmutableList.copyOf(page.get()
                .collect(Collectors.toList()));
    }

    public double getRating(long serviceId) {
        final List<OptionalServiceReview> reviews = serviceReviewRepository.getByServiceId(serviceId);
        if (CollectionUtils.isEmpty(reviews)) {
            return 0;
        }
        final Double rating = reviews.stream()
                .collect(Collectors.averagingDouble(OptionalServiceReview::getStarRating));

        BigDecimal bd = new BigDecimal(Double.toString(rating));
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public boolean exists(long id) {
        return serviceReviewRepository.existsByOptionalService_Id(id);
    }

    public Long count(long id) {
        return serviceReviewRepository.countAllByOptionalService_Id(id);
    }
}
