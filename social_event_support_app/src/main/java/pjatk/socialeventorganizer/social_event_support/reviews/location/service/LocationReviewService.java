package pjatk.socialeventorganizer.social_event_support.reviews.location.service;

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
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.repository.LocationRepository;
import pjatk.socialeventorganizer.social_event_support.reviews.location.model.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.location.model.dto.LocationReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.location.repository.LocationReviewRepository;
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

    private final CustomerRepository customerRepository;

    private final LocationRepository locationRepository;

    public void save(LocationReview locationReview) {
        locationReviewRepository.save(locationReview);
    }

    public LocationReview leaveLocationReview(long id, long locationId, LocationReviewDto dto) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " does not exist"));

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

    public List<LocationReview> getByLocationId(CustomPage paging, long id) {
        if (!exists(id)) {
            throw new NotFoundException("Location with id " + id + " does not exist");
        }
        final Pageable pageable = PageRequest.of(paging.getPageNo(), paging.getPageSize(),
                Sort.by(paging.getSortBy()));
        final Page<LocationReview> page = locationReviewRepository.getByLocationId(id, pageable);

        return ImmutableList.copyOf(page.get()
                .collect(Collectors.toList()));
    }

    public List<LocationReview> getByLocationId(long id) {
        if (!exists(id)) {
            throw new NotFoundException("Location with id " + id + " does not exist");
        }

        return locationReviewRepository.getByLocationId(id);
    }

    public boolean exists(long id) {
        return locationReviewRepository.existsLocationReviewByLocation_Id(id);
    }

    public Long count(long locationId) {
        return locationReviewRepository.countLocationReviewsByLocation_Id(locationId);
    }
}
