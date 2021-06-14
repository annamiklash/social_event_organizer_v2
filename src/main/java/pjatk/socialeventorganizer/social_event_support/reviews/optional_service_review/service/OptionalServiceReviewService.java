package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.dto.OptionalServiceReview;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.request.OptionalServiceReviewRequest;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.response.OptionalServiceReviewInformationResponse;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.mapper.OptionalServiceReviewMapper;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.repository.OptionalServiceReviewRepository;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceReviewService {

    private OptionalServiceReviewRepository repository;
    private OptionalServiceReviewMapper mapper;
    private SecurityService securityService;
    private UserService userService;


    public ImmutableList<OptionalServiceReviewInformationResponse> findAll() {
        final List<OptionalServiceReview> optionalServiceReviewList = repository.findAll();
        return optionalServiceReviewList.stream()
                .map(optionalServiceReview -> mapper.mapDTOtoOptionalServiceReviewInformationResponse(optionalServiceReview))
                .collect(ImmutableList.toImmutableList());
    }

    @Transactional
    public void addNewOptionalServiceReview(OptionalServiceReviewRequest request) {

        final UserCredentials userCredentials = securityService.getUserCredentials();
        final User user = userService.getUserById(userCredentials.getUserId());
        final Integer costumerId = Math.toIntExact(user.getId());
        final OptionalServiceReview optionalServiceReview = mapper.mapToDTO(request, costumerId);
        final OptionalServiceReview savedOptionalServiceReview = saveOptionalServiceReview(optionalServiceReview);

        mapper.mapToResponse(savedOptionalServiceReview);
    }

    public OptionalServiceReview findById(Long id) {
        final Optional<OptionalServiceReview> optionalOptionalServiceReview = repository.findById(id);
        if (optionalOptionalServiceReview.isPresent()) {
            return optionalOptionalServiceReview.get();
        }
        throw new NotFoundException("No optional Service with id " + id);
    }
    

    public ImmutableList<OptionalServiceReviewInformationResponse> getOptionalServiceReviewByOptionalServiceId(Long optionalServiceId) {
        log.info("FETCHING CATERING REVIEWS OF CATERING WITH ID " + optionalServiceId);
        final List<OptionalServiceReview> optionalServiceReviewList = repository.findOptionalServiceReviewByOptionalServiceId(optionalServiceId);
        return optionalServiceReviewList.stream()
                .map(optionalServiceReview -> mapper.mapDTOtoOptionalServiceReviewInformationResponse(optionalServiceReview))
                .collect(ImmutableList.toImmutableList());
    }

    public OptionalServiceReview saveOptionalServiceReview(OptionalServiceReview optionalServiceReview) {
        log.info("TRYING TO SAVE" + optionalServiceReview.toString());

        return repository.saveAndFlush(optionalServiceReview);
    }
}


