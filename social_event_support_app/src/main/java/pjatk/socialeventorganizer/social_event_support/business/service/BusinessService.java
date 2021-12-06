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
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;
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

    private final BusinessRepository businessRepository;

    private final AddressService addressService;

    private final UserService userService;

    private final LocationService locationService;

    private final CateringService cateringService;

    private final OptionalServiceService optionalServiceService;

    public ImmutableList<Business> list(CustomPage customPage) {

        final Pageable paging = PageRequest.of(customPage.getFirstResult(), customPage.getMaxResult(), Sort.by(customPage.getSort()).descending());
        final Page<Business> page = businessRepository.findAll(paging);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    @Transactional(rollbackOn = Exception.class)
    public Business createBusinessAccount(BusinessDto businessDto) {

        final Address address = addressService.create(businessDto.getAddress());
        final Business business = BusinessMapper.fromDto(businessDto);
        final User user = userService.get(businessDto.getUser().getId());

        business.setId(user.getId());
        business.setVerificationStatus(NOT_VERIFIED.name());
        business.setUser(user);
        business.setAddress(address);

        user.setActive(true);
        user.setModifiedAt(LocalDateTime.now());

        userService.save(user);

        log.info("TRYING TO SAVE BUSINESS");
        businessRepository.save(business);

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

    @Transactional(rollbackOn = Exception.class)
    public void deleteLogical(long id) {
        final Business businessToDelete = businessRepository.findAllBusinessInformation(id)
                .orElseThrow(() -> new NotFoundException("Business with id " + id + " DOES NOT EXIST"));

        ImmutableList.copyOf(businessToDelete.getLocations())
                .forEach(location -> locationService.deleteLogical(location.getId()));

        ImmutableList.copyOf(businessToDelete.getCaterings())
                .forEach(catering -> cateringService.deleteLogical(catering.getId()));

        ImmutableList.copyOf(businessToDelete.getServices())
                .forEach(service -> optionalServiceService.deleteLogical(service.getId()));

        addressService.delete(businessToDelete.getAddress());
        userService.delete(businessToDelete.getUser());

        save(businessToDelete);
    }


    public Business verify(long id) {
        final Business business = get(id);
        business.setVerificationStatus(String.valueOf(BusinessVerificationStatusEnum.VERIFIED));

        businessRepository.save(business);
        return business;
    }

    private void save(Business business) {
        businessRepository.save(business);
    }
}
