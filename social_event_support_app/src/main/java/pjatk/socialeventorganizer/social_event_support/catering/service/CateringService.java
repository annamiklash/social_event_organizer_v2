package pjatk.socialeventorganizer.social_event_support.catering.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.availability.catering.model.CateringAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.catering.repository.CateringAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.service.BusinessService;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.service.CateringBusinessHoursService;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringMapper;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto;
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringItemRepository;
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringRepository;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.exceptions.BusinessVerificationException;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.InvalidCredentialsException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.availability.AvailabilityEnum.AVAILABLE;

@Service
@AllArgsConstructor
@Slf4j
public class CateringService {

    private final CateringRepository cateringRepository;

    private final CateringItemRepository cateringItemRepository;

    private final LocationService locationService;

    private final AddressService addressService;

    private final SecurityService securityService;

    private final BusinessService businessService;

    private final CateringBusinessHoursService cateringBusinessHoursService;

    private final CateringAvailabilityRepository cateringAvailabilityRepository;

    public ImmutableList<Catering> list(CustomPage customPagination, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageRequest.of(customPagination.getFirstResult(), customPagination.getMaxResult(),
                Sort.by(customPagination.getSort()).descending());
        final Page<Catering> page = cateringRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    public Catering get(long id) {
        final Optional<Catering> optionalCatering = cateringRepository.findById(id);
        if (optionalCatering.isPresent()) {
            return optionalCatering.get();
        }
        throw new NotFoundException("Address with id " + id + " DOES NOT EXIST");
    }

    public Catering getWithDetail(long id) {
        final Optional<Catering> optionalCatering = cateringRepository.findByIdWithDetail(id);
        if (optionalCatering.isPresent()) {
            return optionalCatering.get();
        }
        throw new IllegalArgumentException("Catering with ID " + id + " does not exist");

    }

    @Transactional
    public Catering create(CateringDto dto, long locationId) {
        final UserCredentials userCredentials = securityService.getUserCredentials();

        final Business business = businessService.get(userCredentials.getUserId());

        if (!business.getVerificationStatus().equals(String.valueOf(BusinessVerificationStatusEnum.VERIFIED))) {
            throw new BusinessVerificationException(BusinessVerificationException.Enum.BUSINESS_NOT_VERIFIED);
        }

        if (!locationService.exists(locationId)) {
            throw new IllegalArgumentException("Location does not exist");
        }

        final Location location = locationService.getWithDetail(locationId);

        if (!business.getId().equals(location.getBusiness().getId())) {
            throw new InvalidCredentialsException(InvalidCredentialsException.Enum.INCORRECT_CREDENTIALS);
        }

        final Address address = addressService.create(dto.getAddress());

        final List<CateringBusinessHours> businessHours = cateringBusinessHoursService.create(dto.getBusinessHours());

        final Catering catering = CateringMapper.fromDto(dto);

        catering.setCateringAddress(address);
        catering.setBusiness(business);
        catering.setCateringBusinessHours(new HashSet<>(businessHours));
        catering.setCreatedAt(LocalDateTime.now());
        catering.setModifiedAt(LocalDateTime.now());
        catering.setLocations(new HashSet<>());

        saveCatering(catering);

        if (dto.isOffersOutsideCatering()) {
            addCateringToLocationsWithSameCity(catering);
        } else {
            addCateringToGivenLocation(catering, locationId);
        }
        return catering;
    }

    @Transactional
    public void addCateringToGivenLocation(Catering catering, long locationId) {

        final Location location = locationService.get(locationId);

        catering.addLocation(location);
        saveCatering(catering);
    }

    @Transactional
    public void addCateringToLocationsWithSameCity(Catering savedCatering) {
        final String city = savedCatering.getCateringAddress().getCity();
        final ImmutableList<Location> locations = locationService.findByCityWithId(city);
        for (Location location : locations) {
            log.info("CATERING ID " + savedCatering.getId() + ", LOCATION ID " + location.getId());
            location.addCatering(savedCatering);
            locationService.saveLocation(location);
        }
    }

    public Catering edit(long cateringId, CateringDto dto) {
        final Catering catering = get(cateringId);

        catering.setEmail(dto.getEmail());
        catering.setName(dto.getName());
        catering.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()));
        catering.setServiceCost(Converter.convertPriceString(dto.getServiceCost()));
        catering.setDescription(dto.getDescription());
        catering.setModifiedAt(LocalDateTime.now());

        cateringRepository.save(catering);
        log.info("UPDATED");

        return catering;
    }

    public Catering editAddress(long cateringId, AddressDto addressDto) {
        if (!cateringWithIdExists(cateringId)) {
            throw new IllegalArgumentException("Catering with ID " + cateringId + " does not exist");
        }
        final Catering catering = get(cateringId);
        final Address address = addressService.edit(catering.getCateringAddress().getId(), addressDto);
        catering.setCateringAddress(address);

        saveCatering(catering);

        return catering;
    }

    public Address getAddress(long cateringId) {
        final Catering catering = get(cateringId);
        return catering.getCateringAddress();
    }


    @Transactional
    public void deleteCatering(Long id) {
        final Catering catering = getWithDetail(id);

        final List<CateringItem> items = catering.getCateringItems().stream()
                .map(cateringItem -> cateringItemRepository.getById(cateringItem.getId()))
                .collect(Collectors.toList());

        items.forEach(cateringItem -> cateringItemRepository.delete(cateringItem));

        addressService.delete(catering.getCateringAddress().getId());

        catering.getLocations().forEach(catering::removeLocation);

        catering.setDeletedAt(LocalDateTime.now());
        log.info("TRYING TO DELETE CATERING WITH ID " + id);
    }


    public boolean cateringWithIdExists(Long id) {
        log.info("CHECKING IF CATERING WITH ID " + id + " EXISTS");
        return cateringRepository.existsById(id);
    }

    public Catering get(Long id) {
        log.info("FETCHING CATERING WITH ID " + id);
        final Optional<Catering> optionalCatering = cateringRepository.findById(id);
        if (optionalCatering.isPresent()) {
            return optionalCatering.get();
        }
        throw new NotFoundException("No catering with id " + id + " found.");
    }

    public Catering saveCatering(Catering catering) {
        log.info("TRYING TO SAVE" + catering.toString());

        return cateringRepository.saveAndFlush(catering);
    }

    public Catering getWithAvailability(long id, String date) {
        final Optional<Catering> optionalCatering = cateringRepository.getByIdWithAvailability(id, date);

        if (optionalCatering.isPresent()) {
            return optionalCatering.get();
        }
        throw new NotFoundException("Catering with id " + id + " DOES NOT EXIST");
    }

    @Transactional
    public void addAvailability(List<AvailabilityDto> dtos, long cateringId) {
        final Catering catering = get(cateringId);

        final List<CateringAvailability> availabilities = dtos.stream()
                .map(AvailabilityMapper::fromDtoToCateringAvailability)
                .collect(Collectors.toList());

        availabilities.stream()
                .peek(availability -> availability.setStatus(AVAILABLE.toString()))
                .peek(availability -> availability.setCatering(catering))
                .forEach(availability -> cateringAvailabilityRepository.save(availability));

    }

}
