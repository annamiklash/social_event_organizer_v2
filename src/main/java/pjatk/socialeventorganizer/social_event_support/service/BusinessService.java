package pjatk.socialeventorganizer.social_event_support.service;


import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.mapper.BusinessMapper;
import pjatk.socialeventorganizer.social_event_support.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.model.dto.Business;
import pjatk.socialeventorganizer.social_event_support.model.exception.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.model.exception.InvalidCredentialsException;
import pjatk.socialeventorganizer.social_event_support.model.request.AddressRequest;
import pjatk.socialeventorganizer.social_event_support.model.request.BusinessRequest;
import pjatk.socialeventorganizer.social_event_support.model.request.LoginRequest;
import pjatk.socialeventorganizer.social_event_support.model.response.BusinessResponse;
import pjatk.socialeventorganizer.social_event_support.repository.BusinessRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BusinessService {

    BusinessRepository businessRepository;

    AddressService addressService;

    AddressMapper addressMapper;

    BusinessMapper businessMapper;


    public ImmutableList<Business> findAll() {
        final List<Business> businessList = businessRepository.findAll();
        return ImmutableList.copyOf(businessList);
    }

    @Transactional
    public BusinessResponse addNewBusiness(BusinessRequest businessRequest) {
        final AddressRequest addressRequest = businessRequest.getAddressRequest();
        final Address address = addressMapper.mapToDTO(addressRequest);

        final Business business = businessMapper.mapToDTO(businessRequest);
        business.setAddress(address);

        log.info("TRYING TO SAVE BUSINESS");
        businessRepository.save(business);
        return businessMapper.mapToResponse(business);
    }

    public Business getBusinessByEmailAndPassword(LoginRequest loginRequest) {
        final Optional<Business> optionalBusiness = businessRepository.findBusinessByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (optionalBusiness.isPresent()) {
            return optionalBusiness.get();
        }
        throw new InvalidCredentialsException("Please check log in credentials");
    }

    public Business getBusinessById(long id) {
        final Optional<Business> businessOptional = businessRepository.findById(id);
        if (businessOptional.isPresent()) {
            return businessOptional.get();
        }
        throw new IllegalArgumentException("Business with ID " + id + " DOES NOT EXIST");

    }

}
