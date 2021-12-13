package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.dto.CateringReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.repository.CateringReviewRepository;
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

    public List<CateringReview> getByCateringId(long id) {
        if (exists(id)) {
            return cateringReviewRepository.getByCateringId(id);
        }
        throw new NotFoundException("Catering with id " + id + " does not exist");

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

    public boolean exists(long id) {
        return cateringReviewRepository.existsCateringReviewByCatering_Id(id);
    }
}
