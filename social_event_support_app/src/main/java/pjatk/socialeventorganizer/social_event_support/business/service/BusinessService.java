package pjatk.socialeventorganizer.social_event_support.business.service;


import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.business.mapper.BusinessMapper;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto;
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper;
import pjatk.socialeventorganizer.social_event_support.common.mapper.PageableMapper;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.common.util.CollectionUtil;
import pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.transaction.Transactional;
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

    private final TimestampHelper timestampHelper;

    public ImmutableList<Business> list(CustomPage customPage) {
        final Pageable paging = PageableMapper.map(customPage);
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
        user.setModifiedAt(timestampHelper.now());

        userService.save(user);

        log.info("TRYING TO SAVE BUSINESS");
        businessRepository.save(business);

        return business;
    }

    public Business edit(long businessId, BusinessDto businessDto) {
        final Business business = businessRepository.getWithAddress(businessId)
                .orElseThrow(() -> new NotFoundException("Address with id " + businessId + " DOES NOT EXIST"));

        business.setBusinessName(businessDto.getBusinessName());
        business.setFirstName(businessDto.getFirstName());
        business.setLastName(businessDto.getLastName());
        business.setPhoneNumber(Converter.convertPhoneNumberString(businessDto.getPhoneNumber()));

        addressService.edit(business.getAddress().getId(), businessDto.getAddress());

        businessRepository.save(business);

        return business;
    }

    public Business getWithDetail(long businessId) {
        return businessRepository.getWithDetail(businessId)
                .orElseThrow(() -> new NotFoundException("Business with businessId " + businessId + " DOES NOT EXIST"));

    }

    public Business get(long businessId) {
        return businessRepository.findById(businessId)
                .orElseThrow(() -> new NotFoundException("Business with businessId " + businessId + " DOES NOT EXIST"));
    }



    @Transactional(rollbackOn = Exception.class)
    public void deleteLogical(long businessId) {
        final Business businessToDelete = businessRepository.findAllBusinessInformation(businessId)
                .orElseThrow(() -> new NotFoundException("Business with businessId " + businessId + " DOES NOT EXIST"));

        CollectionUtil.emptyListIfNull(businessToDelete.getLocations())
                .forEach(location -> locationService.deleteLogical(location.getId()));

        CollectionUtil.emptyListIfNull(businessToDelete.getCaterings())
                .forEach(catering -> cateringService.deleteLogical(catering.getId()));

        CollectionUtil.emptyListIfNull(businessToDelete.getServices())
                .forEach(service -> optionalServiceService.deleteLogical(service.getId()));

        addressService.delete(businessToDelete.getAddress());
        userService.delete(businessToDelete.getUser());

        businessRepository.save(businessToDelete);
    }


    public Business verify(long id) {
        final Business business = get(id);
        business.setVerificationStatus(String.valueOf(BusinessVerificationStatusEnum.VERIFIED));

        businessRepository.save(business);
        return business;
    }


    private Business getWithAddress(long id) {
        return businessRepository.findByIdWithAddress(id)
                .orElseThrow(() -> new NotFoundException("Address with id " + id + " DOES NOT EXIST"));
    }
}
