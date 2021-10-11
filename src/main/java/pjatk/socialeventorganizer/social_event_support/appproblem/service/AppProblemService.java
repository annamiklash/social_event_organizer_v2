package pjatk.socialeventorganizer.social_event_support.appproblem.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.appproblem.mapper.AppProblemMapper;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblem;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.request.AppProblemRequest;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.response.AppProblemInformationResponse;
import pjatk.socialeventorganizer.social_event_support.appproblem.repository.AppProblemRepository;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.model.response.CateringInformationResponse;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.Location;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.dto.LocationReview;
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
public class AppProblemService {

    AppProblemRepository repository;

    AppProblemMapper appProblemMapper;

    private final SecurityService securityService;

    private final UserService userService;


    public ImmutableList<AppProblemInformationResponse> findAll() {
        final List<AppProblem> appProblemList = repository.findAll();
        return appProblemList.stream()
                .map(appProblem -> appProblemMapper.mapDTOtoAppProblemInformationResponse(appProblem))
                .collect(ImmutableList.toImmutableList());
    }

    @Transactional
    public void addNewAppProblem(AppProblemRequest appProblemRequest) {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        final User user = userService.getUserById(userCredentials.getUserId());
        final Integer userId = Math.toIntExact(user.getId());
        final AppProblem appProblem = appProblemMapper.mapToDTO(appProblemRequest, userId);
        final AppProblem savedAppProblem = saveAppProblem(appProblem);
        log.info("TRYING TO SAVE CUSTOMER");
        //repository.save(appProblem);
        appProblemMapper.mapDTOtoAppProblemResponse(savedAppProblem);
    }

    @Transactional
    public void updateAppProblem(Long appProblemId, AppProblemRequest request) {
        if (!appProblemWithIdExists(appProblemId)) {
            throw new IllegalArgumentException("AppProblem with ID " + appProblemId + " does not exist");
        }
        final AppProblem fetchedAppProblem = findById(appProblemId);

        appProblemMapper.updateDTO(request, fetchedAppProblem);
//        final Address address = fetchedAppProblem.getAppProblemAddress();
//        if (!addressService.addressWithIdExists(addressId) || !address.getId().equals(addressId)) {
//            throw new IllegalArgumentException("Address with ID " + addressId + " does not exist or does not correspond to appProblem");
//        }
        log.info("TRYING TO UPDATE " + fetchedAppProblem);
//        final AddressRequest addressRequest = request.getAddressRequest();
//        final Address updatedAddress = addressMapper.updateMapToDTO(addressRequest, addressId);
//        fetchedAppProblem.setAppProblemAddress(updatedAddress);
        repository.save(fetchedAppProblem);
        log.info("UPDATED");
    }

    public AppProblem findById(Long id) {
        final Optional<AppProblem> optionalAppProblem = repository.findById(id);
        if (optionalAppProblem.isPresent()) {
            return optionalAppProblem.get();
        }
        throw new NotFoundException("No app problem with id " + id);
    }


    public void deleteAppProblem(Long id) {
        if (!appProblemWithIdExists(id)) {
            throw new IllegalArgumentException("AppProblem with ID " + id + " does not exist");
        }
        repository.deleteById(id);
        log.info("TRYING TO DELETE APPPROBLEM WITH ID " + id);
    }


    public boolean appProblemWithIdExists(Long id) {
        log.info("CHECKING IF APP_PROBLEM WITH ID " + id + " EXISTS");
        return repository.existsById(id);
    }


    public AppProblem saveAppProblem(AppProblem appProblem) {
        log.info("TRYING TO SAVE" + appProblem.toString());

        return repository.saveAndFlush(appProblem);
    }

    public ImmutableList<AppProblemInformationResponse> findByUserId(Integer userId) {
        final List<AppProblem> appProblemList = repository.findByUserId(userId);
        if (appProblemList != null && !appProblemList.isEmpty()) {
            return appProblemList.stream()
                    .map(appProblem -> appProblemMapper.mapDTOtoAppProblemInformationResponse(appProblem))
                    .collect(ImmutableList.toImmutableList());
        } else {
            throw new NotFoundException("No problems submitted from " + userId + " were found");
        }
    }
}
