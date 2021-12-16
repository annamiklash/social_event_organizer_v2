package pjatk.socialeventorganizer.social_event_support.reviews.catering.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.reviews.catering.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering.model.dto.CateringReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.catering.repository.CateringReviewRepository;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CateringReviewService {

    private final CateringReviewRepository cateringReviewRepository;

    private final CustomerService customerService;

    private final CateringService cateringService;

    public void save(CateringReview cateringReview) {
        cateringReviewRepository.save(cateringReview);
    }

    public CateringReview leaveCateringReview(long id, long cateringId, CateringReviewDto dto) {
        final Customer customer = customerService.get(id);

        final Catering catering = cateringService.get(cateringId);

        final CateringReview cateringReview = ReviewMapper.fromCateringReviewDto(dto);
        cateringReview.setCatering(catering);
        cateringReview.setCustomer(customer);
        cateringReview.setCreatedAt(LocalDateTime.now());

        save(cateringReview);

        return cateringReview;
    }


    public List<CateringReview> getByCateringId(CustomPage paging, long id) {
        if (!exists(id)) {
            throw new NotFoundException("Catering with id " + id + " does not exist");
        }
        final Pageable pageable = PageRequest.of(paging.getPageNo(), paging.getPageSize(),
                Sort.by(paging.getSortBy()));
        final Page<CateringReview> page = cateringReviewRepository.getByCateringId(id, pageable);

        return ImmutableList.copyOf(page.get()
                .collect(Collectors.toList()));
    }

    public List<CateringReview> getByCateringId(long id) {
        if (!exists(id)) {
            throw new NotFoundException("Catering with id " + id + " does not exist");
        }
        return cateringReviewRepository.getByCateringId(id);

        return ImmutableList.copyOf(page.get()
                .collect(Collectors.toList()));
    }

    public double getRating(long cateringId) {
        final List<CateringReview> reviews = cateringReviewRepository.getByCateringId(cateringId);
        if (CollectionUtils.isEmpty(reviews)) {
            return 0;
        }
        final Double rating = reviews.stream()
                .collect(Collectors.averagingDouble(CateringReview::getStarRating));

        BigDecimal bd = new BigDecimal(Double.toString(rating));
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public Long count(long cateringId) {
        return cateringReviewRepository.countAllByCatering_Id(cateringId);
    }

    private boolean exists(long id) {
        return cateringReviewRepository.existsCateringReviewByCatering_Id(id);
    }

}
