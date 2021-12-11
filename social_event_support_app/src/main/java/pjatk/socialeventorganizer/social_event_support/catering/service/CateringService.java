package pjatk.socialeventorganizer.social_event_support.catering.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository;
import pjatk.socialeventorganizer.social_event_support.businesshours.DayEnum;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.service.CateringBusinessHoursService;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringMapper;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.FilterCateringsDto;
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringItemRepository;
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringRepository;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine;
import pjatk.socialeventorganizer.social_event_support.cuisine.model.dto.CuisineDto;
import pjatk.socialeventorganizer.social_event_support.cuisine.service.CuisineService;
import pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.*;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CateringService {

    private final CateringRepository cateringRepository;

    private final CateringItemRepository cateringItemRepository;

    private final LocationService locationService;

    private final AddressService addressService;

    private final SecurityService securityService;

    private final BusinessRepository businessRepository;

    private final CateringBusinessHoursService cateringBusinessHoursService;

    private final CuisineService cuisineService;

    public ImmutableList<Catering> list(CustomPage customPagination, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageRequest.of(customPagination.getPageNo(), customPagination.getPageSize(),
                Sort.by(customPagination.getSortBy()).descending());
        final Page<Catering> page = cateringRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    public ImmutableList<Catering> search(FilterCateringsDto dto) {
        String city = dto.getCity();
        city = Strings.isNullOrEmpty(city) ? null : city.substring(0, dto.getCity().indexOf(','));

        List<Catering> caterings = new ArrayList<>();
        if (dto.getCuisines() != null) {
            final Set<Long> cuisines = dto.getCuisines().stream()
                    .filter(Objects::nonNull)
                    .map(cuisineService::getByName)
                    .map(Cuisine::getId)
                    .collect(Collectors.toSet());
            caterings = cateringRepository.search(cuisines, city);
        } else {
            caterings = cateringRepository.search(null, city);
        }

        if (dto.getDate() != null) {
            final LocalDate date = DateTimeUtil.fromStringToFormattedDate(dto.getDate());
            final String dateDayOfTheWeek = date.getDayOfWeek().name();

            caterings = caterings.stream()
                    .filter(catering -> catering.getCateringBusinessHours()
                            .stream()
                            .anyMatch(cateringBusinessHours -> cateringBusinessHours.getDay().equalsIgnoreCase(dateDayOfTheWeek)))
                    .collect(Collectors.toList());
        }

        caterings = filterByPrice(String.valueOf(dto.getMinPrice()), String.valueOf(dto.getMaxPrice()), caterings);
        return ImmutableList.copyOf(caterings);
    }

    public boolean isOpen(Catering catering, String day) {
        return catering.getCateringBusinessHours().stream()
                .anyMatch(cateringBusinessHours -> cateringBusinessHours.getDay().equals(DayEnum.valueOfLabel(day).name()));
    }

    public Catering get(long id) {
        return cateringRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Catering with ID " + id + " does not exist"));
    }

    public Catering getWithDetail(long id) {
        return cateringRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new IllegalArgumentException("Catering with ID " + id + " does not exist"));
    }

    public ImmutableList<Catering> getByBusinessId(long id) {
        return ImmutableList.copyOf(cateringRepository.findAllByBusiness_Id(id));
    }

    @Transactional(rollbackOn = Exception.class)
    public Catering create(CateringDto cateringDto, Long locationId) {
        final UserCredentials userCredentials = securityService.getUserCredentials();

        final Business business = businessRepository.findById(userCredentials.getUserId())
                .orElseThrow(() -> new NotFoundException("Business with id " + userCredentials.getUserId() + " DOES NOT EXIST"));

        if (!business.getVerificationStatus().equals(String.valueOf(BusinessVerificationStatusEnum.VERIFIED))) {
            throw new BusinessVerificationException(BusinessVerificationException.Enum.BUSINESS_NOT_VERIFIED);
        }
        if (locationId == null) {
            return createStandaloneCatering(cateringDto, business);
        }
        return createCateringWithLocation(cateringDto, locationId, business);

    }

    public Catering getWithBusinessHours(long cateringId) {
        return cateringRepository.getWithBusinessHours(cateringId)
                .orElseThrow(() -> new NotFoundException("Not catering with id " + cateringId));
    }

    @Transactional(rollbackOn = Exception.class)
    public void addCateringToGivenLocation(Catering catering, long locationId) {

        final Location location = locationService.get(locationId);

        catering.addLocation(location);
        saveCatering(catering);
    }

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

    @Transactional(rollbackOn = Exception.class)
    public void deleteLogical(long id) {
        final Catering cateringToDelete = cateringRepository.findAllCateringInformation(id)
                .orElseThrow(() -> new NotFoundException("No catering with id " + id));

        boolean hasPendingReservations = hasPendingReservations(cateringToDelete);
        if (hasPendingReservations) {
            throw new ActionNotAllowedException("Cannot delete catering with reservations pending");
        }

        ImmutableSet.copyOf(cateringToDelete.getCateringBusinessHours())
                .forEach(cateringBusinessHoursService::delete);

        ImmutableSet.copyOf(cateringToDelete.getCateringItems())
                .forEach(cateringItemRepository::delete);

        addressService.delete(cateringToDelete.getCateringAddress().getId());

        cateringToDelete.setModifiedAt(LocalDateTime.now());
        cateringToDelete.setDeletedAt(LocalDateTime.now());

    }

    private List<Catering> filterByPrice(String minPrice, String maxPrice, List<Catering> caterings) {
        if (minPrice == null && maxPrice == null) {
            return caterings;
        } else if (minPrice != null && maxPrice == null) {
            return caterings.stream()
                    .filter(catering -> Converter.convertPriceString(minPrice).compareTo(catering.getServiceCost()) < 0 ||
                            Converter.convertPriceString(minPrice).compareTo(catering.getServiceCost()) == 0)
                    .collect(Collectors.toList());
        } else if (minPrice == null) {
            return caterings.stream()
                    .filter(catering -> Converter.convertPriceString(maxPrice).compareTo(catering.getServiceCost()) > 0 ||
                            Converter.convertPriceString(maxPrice).compareTo(catering.getServiceCost()) == 0)
                    .collect(Collectors.toList());
        } else {
            return caterings.stream()
                    .filter(catering -> Converter.convertPriceString(minPrice).compareTo(catering.getServiceCost()) < 0 ||
                            Converter.convertPriceString(minPrice).compareTo(catering.getServiceCost()) == 0)
                    .filter(catering -> Converter.convertPriceString(maxPrice).compareTo(catering.getServiceCost()) > 0 ||
                            Converter.convertPriceString(maxPrice).compareTo(catering.getServiceCost()) == 0)
                    .collect(Collectors.toList());
        }
    }

    private boolean hasPendingReservations(Catering cateringToDelete) {
        return cateringToDelete.getCateringForChosenEventLocations().stream()
                .map(catering -> catering.getEventLocation().getEvent())
                .anyMatch(organizedEvent -> organizedEvent.getDate().isAfter(LocalDate.now()));
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

    public Catering getWithImages(long cateringId) {
        return cateringRepository.findWithImages(cateringId)
                .orElseThrow(() -> new NotFoundException("Catering with id " + cateringId + " DOES NOT EXIST"));
    }

    public ImmutableList<Catering> getByLocationId(long id) {
        return ImmutableList.copyOf(cateringRepository.findAllByLocationId(id));
    }

    private void saveCatering(Catering catering) {
        log.info("TRYING TO SAVE" + catering.toString());
        cateringRepository.saveAndFlush(catering);
    }

    private Catering createCateringWithLocation(CateringDto dto, Long locationId, Business business) {
        if (!locationService.exists(locationId)) {
            throw new IllegalArgumentException("Location does not exist");
        }

        final Location location = locationService.getWithDetail(locationId);

        if (business.getId() != (location.getBusiness().getId())) {
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

        final List<CuisineDto> cuisineDtos = dto.getCuisines();

        final Set<Cuisine> cuisines = cuisineDtos.stream()
                .map(CuisineDto::getName)
                .map(cuisineService::getByName)
                .collect(Collectors.toSet());

        catering.setCuisines(cuisines);

        if (dto.isOffersOutsideCatering()) {
            addCateringToLocationsWithSameCity(catering);
        } else {
            addCateringToGivenLocation(catering, locationId);
        }
        cateringRepository.save(catering);
        return catering;
    }

    private Catering createStandaloneCatering(CateringDto dto, Business business) {
        if (!dto.isOffersOutsideCatering()) {
            throw new IllegalArgumentException("Standalone catering must offer outside catering");
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

        final List<CuisineDto> cuisineDtos = dto.getCuisines();

        final Set<Cuisine> cuisines = cuisineDtos.stream()
                .map(CuisineDto::getName)
                .map(cuisineService::getByName)
                .collect(Collectors.toSet());

        catering.setCuisines(cuisines);

        addCateringToLocationsWithSameCity(catering);
        cateringRepository.save(catering);

        return catering;
    }


    public Long count(String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();
        return cateringRepository.countAll(keyword);
    }

}
