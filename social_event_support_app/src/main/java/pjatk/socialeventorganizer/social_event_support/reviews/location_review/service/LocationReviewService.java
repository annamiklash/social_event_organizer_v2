package pjatk.socialeventorganizer.social_event_support.reviews.location_review.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.repository.LocationRepository;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.dto.LocationReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.repository.LocationReviewRepository;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class LocationReviewService {

    private final LocationReviewRepository locationReviewRepository;

    private final CustomerService customerService;

    private final LocationRepository locationRepository;

    public void save(LocationReview locationReview) {
        locationReviewRepository.save(locationReview);
    }

    public LocationReview leaveLocationReview(long id, long locationId, LocationReviewDto dto) {
        final Customer customer = customerService.get(id);

        final Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Location with id " + id + " DOES NOT EXIST"));

        final LocationReview locationReview = ReviewMapper.fromLocationReviewDto(dto);
        locationReview.setLocation(location);
        locationReview.setCustomer(customer);
        locationReview.setCreatedAt(LocalDateTime.now());

        save(locationReview);

        return locationReview;
    }

    public double getRating(long locationId) {
        final List<LocationReview> reviews = locationReviewRepository.getByLocationId(locationId);
        if (CollectionUtils.isEmpty(reviews)) {
            return 0;
        }
        final Double rating = reviews.stream()
                .collect(Collectors.averagingDouble(LocationReview::getStarRating));

        BigDecimal bd = new BigDecimal(Double.toString(rating));
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public List<LocationReview> getByLocationId(long id) {
        if (exists(id)) {
            return locationReviewRepository.getByLocationId(id);
        }
        throw new NotFoundException("Location with id " + id + " does not exist");

    }

    public boolean exists(long id) {
        return locationReviewRepository.existsLocationReviewByLocation_Id(id);
    }
}
