package pjatk.socialeventorganizer.social_event_support.reviews.location_review.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.dto.LocationReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.repository.LocationReviewRepository;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LocationReviewService {

    private LocationReviewRepository locationReviewRepository;

    private CustomerService customerService;

    private LocationService locationService;

    public void save(LocationReview locationReview) {
        locationReviewRepository.save(locationReview);
    }

    public LocationReview leaveLocationReview(long id, long locationId, LocationReviewDto dto) {
        final Customer customer = customerService.get(id);

        final Location location = locationService.get(locationId);

        final LocationReview locationReview = ReviewMapper.fromLocationReviewDto(dto);
        locationReview.setLocation(location);
        locationReview.setCustomer(customer);
        locationReview.setCreatedAt(LocalDateTime.now());

        save(locationReview);

        return locationReview;
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
