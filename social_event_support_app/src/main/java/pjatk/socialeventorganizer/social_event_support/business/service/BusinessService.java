package pjatk.socialeventorganizer.social_event_support.business.service;


import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.business.mapper.BusinessMapper;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto;
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.exceptions.ForbiddenAccessException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum.NOT_VERIFIED;

@Service
@AllArgsConstructor
@Slf4j
public class BusinessService {

    BusinessRepository businessRepository;

    AddressService addressService;

    SecurityService securityService;

    UserService userService;

    public ImmutableList<Business> list(CustomPage customPage) {

        final Pageable paging = PageRequest.of(customPage.getFirstResult(), customPage.getMaxResult(), Sort.by(customPage.getSort()).descending());
        final Page<Business> page = businessRepository.findAll(paging);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    @Transactional
    public Business createBusinessAccount(BusinessDto businessDto) {
        final UserCredentials userCredentials = securityService.getUserCredentials();

        if (!userService.isNewAccount(userCredentials.getUserId(), userCredentials.getUserType())) {
            throw new ForbiddenAccessException("Cannot access");
        }

        final Address address = addressService.create(businessDto.getAddress());
        final User user = userService.getById(userCredentials.getUserId());

        final Business business = BusinessMapper.fromDto(businessDto);
        business.setVerificationStatus(NOT_VERIFIED.toString());
        business.setUser(user);
        business.setAddress(address);

        log.info("TRYING TO SAVE BUSINESS");
        businessRepository.save(business);

        //TODO: verify
        user.setModifiedAt(LocalDateTime.now());
        userService.save(user);

        return business;

    }

    public Business getWithDetail(long id) {
        final Optional<Business> businessOptional = businessRepository.getWithDetail(id);
        if (businessOptional.isPresent()) {
            return businessOptional.get();
        }
        throw new NotFoundException("Address with id " + id + " DOES NOT EXIST");
    }

    public Business get(long id) {
        final Optional<Business> businessOptional = businessRepository.findById(id);
        if (businessOptional.isPresent()) {
            return businessOptional.get();
        }
        throw new NotFoundException("Business with id " + id + " DOES NOT EXIST");
    }

    public void delete(long id) {
        final Business business = getWithDetail(id);


        //TODO: finish
        //delete address
        //delete caterings is exist
        //delete locations if exist
        //delete caterings if exist

    }

    public Business verify(long id) {

        final Business business = get(id);
        business.setVerificationStatus(String.valueOf(BusinessVerificationStatusEnum.VERIFIED));

        businessRepository.save(business);
        return business;

    }
}
