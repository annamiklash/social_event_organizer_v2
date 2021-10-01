package pjatk.socialeventorganizer.social_event_support.appproblem.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.request.AddressRequest;
import pjatk.socialeventorganizer.social_event_support.appproblem.mapper.AppProblemMapper;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblem;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.request.AppProblemRequest;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.response.AppProblemInformationResponse;
import pjatk.socialeventorganizer.social_event_support.appproblem.repository.AppProblemRepository;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.model.request.CreateCustomerAccountRequest;
import pjatk.socialeventorganizer.social_event_support.exceptions.ForbiddenAccessException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
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

//    public ImmutableList<AppProblemInformationResponse> findByName(String name) {
//        final List<AppProblem> appProblemList = repository.findByNameContaining(name);
//        if (appProblemList != null && !appProblemList.isEmpty()) {
//            return appProblemList.stream()
//                    .map(appProblem -> appProblemMapper.mapDTOtoAppProblemInformationResponse(appProblem))
//                    .collect(ImmutableList.toImmutableList());
//        } else {
//            throw new NotFoundException("No appProblem with the name " + name + " was found");
//        }
//    }

    public ImmutableList<AppProblemInformationResponse> findByUser(Long userId) {
        final List<AppProblem> appProblemList = repository.findByUserId(userId);
        if (appProblemList != null && !appProblemList.isEmpty()) {
            return appProblemList.stream()
                    .map(appProblem -> appProblemMapper.mapDTOtoAppProblemInformationResponse(appProblem))
                    .collect(ImmutableList.toImmutableList());
        } else {
            throw new NotFoundException("No appProblem for the user with id " + userId + " was found");
        }
    }
    @Transactional
    public void addNewAppProblem(AppProblemRequest appProblemRequest) {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        if (userCredentials.getIsNewAccount()) {
             log.info("new account"+ userCredentials.getUserId());//throw new ForbiddenAccessException("Cannot access");
        }
//        final AddressRequest addressRequest = customerRequest.getAddressRequest();
//        final Address address = addressMapper.mapToDTO(addressRequest);
//        addressService.save(address);

        final User userById = userService.getUserById(userCredentials.getUserId());
        log.info("account id is"+ userById.getId());
        final AppProblem appProblem = appProblemMapper.mapToDTO(appProblemRequest, userById);

        log.info("TRYING TO SAVE CUSTOMER");
        repository.save(appProblem);
    }

//    @Transactional
//    public void addNewAppProblem(AppProblemRequest request, User user) {
//
//        final AppProblem appProblem = appProblemMapper.mapToDTO(request, user);
//        final AppProblem savedAppProblem = saveAppProblem(appProblem);
//
////        if (request.isOffersOutsideAppProblem()) {
////            addAppProblemToLocationsWithSameCity(savedAppProblem);
////        } else {
////            addAppProblemToGivenLocation(savedAppProblem, request.getLocationId());
////        }
//        appProblemMapper.mapDTOtoAppProblemResponse(savedAppProblem);
//    }

//    @Transactional
//    public void addAppProblemToGivenLocation(AppProblem savedAppProblem, long locationId) {
//        final Location location = locationService.findById(locationId);
//        location.addAppProblem(savedAppProblem);
//        locationService.saveLocation(location);
//    }
//
//    @Transactional
//    public void addAppProblemToLocationsWithSameCity(AppProblem savedAppProblem) {
//        final String city = savedAppProblem.getAppProblemAddress().getCity();
//        final ImmutableList<Location> locations = locationService.findByCityWithId(city);
//        for (Location location : locations) {
//            log.info("CATERING ID " + savedAppProblem.getId() + ", LOCATION ID " + location.getId());
//            location.addAppProblem(savedAppProblem);
//            locationService.saveLocation(location);
//        }
//    }
//
//    @Transactional
//    public void updateAppProblem(Long appProblemId, AppProblemRequest request, Long addressId) {
//        if (!appProblemWithIdExists(appProblemId)) {
//            throw new IllegalArgumentException("AppProblem with ID " + appProblemId + " does not exist");
//        }
//        final AppProblem fetchedAppProblem = getAppProblemById(appProblemId);
//
//        appProblemMapper.updateDTO(request, fetchedAppProblem);
//        final Address address = fetchedAppProblem.getAppProblemAddress();
//        if (!addressService.addressWithIdExists(addressId) || !address.getId().equals(addressId)) {
//            throw new IllegalArgumentException("Address with ID " + addressId + " does not exist or does not correspond to appProblem");
//        }
//        log.info("TRYING TO UPDATE " + fetchedAppProblem);
//        final AddressRequest addressRequest = request.getAddressRequest();
//        final Address updatedAddress = addressMapper.updateMapToDTO(addressRequest, addressId);
//        fetchedAppProblem.setAppProblemAddress(updatedAddress);
//        repository.save(fetchedAppProblem);
//        log.info("UPDATED");
//    }

    public void deleteAppProblem(Long id) {
        if (!appProblemWithIdExists(id)) {
            throw new IllegalArgumentException("AppProblem with ID " + id + " does not exist");
        }
        repository.deleteById(id);
        log.info("TRYING TO DELETE APPPROBLEM WITH ID " + id);
    }


    public boolean appProblemWithIdExists(Long id) {
        log.info("CHECKING IF APPPROBLEM WITH ID " + id + " EXISTS");
        return repository.existsById(id);
    }

    public AppProblem getAppProblemById(Long id) {
        log.info("FETCHING APPPROBLEM WITH ID " + id);
        final Optional<AppProblem> optionalAppProblem = repository.findById(id);
        if (optionalAppProblem.isPresent()) {
            return optionalAppProblem.get();
        }
        throw new NotFoundException("No App Problem with id " + id + " found.");
    }

    public AppProblem saveAppProblem(AppProblem appProblem) {
        log.info("TRYING TO SAVE" + appProblem.toString());

        return repository.saveAndFlush(appProblem);
    }

}
