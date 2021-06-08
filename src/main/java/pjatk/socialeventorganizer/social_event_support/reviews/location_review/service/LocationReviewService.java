package pjatk.socialeventorganizer.social_event_support.reviews.location_review.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.response.LocationReviewInformationResponse;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.mapper.LocationReviewMapper;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.dto.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.request.LocationReviewRequest;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.repository.LocationReviewRepository;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class LocationReviewService {

    private LocationReviewRepository repository;
    private LocationReviewMapper mapper;
    private SecurityService securityService;
    private UserService userService;


    public ImmutableList<LocationReviewInformationResponse> findAll() {
        final List<LocationReview> locationReviewList = repository.findAll();
        return locationReviewList.stream()
                .map(locationReview -> mapper.mapDTOtoLocationReviewInformationResponse(locationReview))
                .collect(ImmutableList.toImmutableList());
    }

    @Transactional
    public void addNewLocationReview(LocationReviewRequest request) {

        final UserCredentials userCredentials = securityService.getUserCredentials();
        final User user = userService.getUserById(userCredentials.getUserId());
        final Integer costumerId = Math.toIntExact(user.getId());
        final LocationReview locationReview = mapper.mapToDTO(request, costumerId);
        final LocationReview savedLocationReview = saveLocationReview(locationReview);

        mapper.mapToResponse(savedLocationReview);
    }

    public ImmutableList<LocationReviewInformationResponse> getLocationReviewByLocationId(Long locationId) {
        log.info("FETCHING CATERING REVIEWS OF CATERING WITH ID " + locationId);
        final List<LocationReview> locationReviewList = repository.findLocationReviewByLocationId(locationId);
        return locationReviewList.stream()
                .map(locationReview -> mapper.mapDTOtoLocationReviewInformationResponse(locationReview))
                .collect(ImmutableList.toImmutableList());
    }

    public LocationReview saveLocationReview(LocationReview locationReview) {
        log.info("TRYING TO SAVE" + locationReview.toString());

        return repository.saveAndFlush(locationReview);
    }
}


