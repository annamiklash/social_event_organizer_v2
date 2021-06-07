package pjatk.socialeventorganizer.social_event_support.business.service;


import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.request.AddressRequest;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.business.mapper.BusinessMapper;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.Business;
import pjatk.socialeventorganizer.social_event_support.business.model.request.CreateBusinessAccountRequest;
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.ForbiddenAccessException;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BusinessService {

    BusinessRepository businessRepository;

    BusinessMapper businessMapper;

    AddressService addressService;

    AddressMapper addressMapper;

    SecurityService securityService;

    UserService userService;

    public ImmutableList<Business> findAll() {
        final List<Business> businessList = businessRepository.findAll();
        return ImmutableList.copyOf(businessList);
    }

    @Transactional
    public void createBusinessAccount(CreateBusinessAccountRequest businessRequest) {
        final UserCredentials userCredentials = securityService.getUserCredentials();

        if (!userCredentials.getIsNewAccount()) {
            throw new ForbiddenAccessException("Cannot access");
        }
        final AddressRequest addressRequest = businessRequest.getAddressRequest();
        final Address address = addressMapper.mapToDTO(addressRequest);

        final User userById = userService.getUserById(userCredentials.getUserId());

        final Business business = businessMapper.mapToDTO(businessRequest, userById);
        business.setAddress(address);

        log.info("TRYING TO SAVE BUSINESS");
        businessRepository.save(business);

        userCredentials.setIsNewAccount(false);
    }

}
