package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.mapper.CateringReviewMapper;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.dto.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.request.CateringReviewRequest;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.response.CateringReviewInformationResponse;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.repository.CateringReviewRepository;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class CateringReviewService {

    private CateringReviewRepository repository;
    private CateringReviewMapper mapper;
    private SecurityService securityService;
    private UserService userService;


    public ImmutableList<CateringReviewInformationResponse> findAll() {
        final List<CateringReview> cateringReviewList = repository.findAll();
        return cateringReviewList.stream()
                .map(cateringReview -> mapper.mapDTOtoCateringReviewInformationResponse(cateringReview))
                .collect(ImmutableList.toImmutableList());
    }

    @Transactional
    public void addNewCateringReview(CateringReviewRequest request) {

        final UserCredentials userCredentials = securityService.getUserCredentials();
        final User user = userService.getUserById(userCredentials.getUserId());
        final Integer costumerId = Math.toIntExact(user.getId());
        final CateringReview cateringReview = mapper.mapToDTO(request, costumerId);
        final CateringReview savedCateringReview = saveCateringReview(cateringReview);

        mapper.mapToResponse(savedCateringReview);
    }

    public ImmutableList<CateringReviewInformationResponse> getCateringReviewByCateringId(Long cateringId) {
        log.info("FETCHING CATERING REVIEWS OF CATERING WITH ID " + cateringId);
        final List<CateringReview> cateringReviewList = repository.findCateringReviewByCateringId(cateringId);
        return cateringReviewList.stream()
                .map(cateringReview -> mapper.mapDTOtoCateringReviewInformationResponse(cateringReview))
                .collect(ImmutableList.toImmutableList());
    }

    public CateringReview saveCateringReview(CateringReview cateringReview) {
        log.info("TRYING TO SAVE" + cateringReview.toString());

        return repository.saveAndFlush(cateringReview);
    }
}


