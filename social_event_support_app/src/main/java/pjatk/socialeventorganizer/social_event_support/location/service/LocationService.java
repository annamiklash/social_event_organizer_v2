package pjatk.socialeventorganizer.social_event_support.location.service;

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
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.service.BusinessService;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringRepository;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.enums.LocationDescriptionItemEnum;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.initial_booking.EventBookDateDto;
import pjatk.socialeventorganizer.social_event_support.exceptions.BusinessVerificationException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.mapper.LocationAvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.location.mapper.LocationMapper;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.model.LocationAvailability;
import pjatk.socialeventorganizer.social_event_support.location.model.LocationDescriptionItem;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.FilterLocationsDto;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationAvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto;
import pjatk.socialeventorganizer.social_event_support.location.repository.LocationAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.location.repository.LocationRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.location.enums.LocationAvailabilityEnum.AVAILABLE;
import static pjatk.socialeventorganizer.social_event_support.location.enums.LocationAvailabilityEnum.NOT_AVAILABLE;

@Service
@AllArgsConstructor
@Slf4j
public class LocationService {

    private LocationRepository locationRepository;

    private LocationDescriptionItemService locationDescriptionItemService;

    private CateringRepository cateringRepository;

    private AddressService addressService;

    private BusinessService businessService;

    private LocationAvailabilityRepository locationAvailabilityRepository;


    public ImmutableList<Location> list(CustomPage customPagination, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageRequest.of(customPagination.getFirstResult(), customPagination.getMaxResult(), Sort.by(customPagination.getSort()).descending());
        final Page<Location> page = locationRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }


    public Location get(long id) {
        final Optional<Location> optionalLocation = locationRepository.findById(id);
        if (optionalLocation.isPresent()) {
            return optionalLocation.get();
        }
        throw new NotFoundException("Location with id " + id + " DOES NOT EXIST");
    }


    public Location getWithDetail(long id) {
        final Optional<Location> optionalLocation = locationRepository.getByIdWithDetail(id);
        if (optionalLocation.isPresent()) {
            return optionalLocation.get();
        }
        throw new NotFoundException("Location with id " + id + " DOES NOT EXIST");
    }


    public boolean exists(long id) {
        return locationRepository.existsById(id);
    }

    public ImmutableList<Location> search(FilterLocationsDto dto) {

        final List<LocationDescriptionItem> filters = dto.getDescriptionItems().stream()
                .map(item -> locationDescriptionItemService.getByName(item))
                .collect(Collectors.toList());

        List<Location> locations = locationRepository.search(dto.getDate(), dto.getTimeFrom(), dto.getTimeTo());

        locations = locations.stream()
                .filter(location -> location.getDescriptions().containsAll(filters))
                .collect(Collectors.toList());


        if (dto.getIsSeated() == null && dto.getGuestCount() != null) {
            return ImmutableList.copyOf(
                    locations.stream()
                            .filter(location -> dto.getGuestCount() <= location.getSeatingCapacity() + location.getStandingCapacity())
                            .collect(Collectors.toList()));
        } else if (dto.getIsSeated() != null && dto.getIsSeated() != null) {

            if (dto.getIsSeated()) {
                return ImmutableList.copyOf(
                        locations.stream()
                                .filter(location -> dto.getGuestCount() <= location.getSeatingCapacity())
                                .collect(Collectors.toList()));
            }

            if (!dto.getIsSeated()) {
                return ImmutableList.copyOf(
                        locations.stream()
                                .filter(location -> dto.getGuestCount() <= location.getStandingCapacity())
                                .collect(Collectors.toList()));
            }
        }

        return ImmutableList.copyOf(locations);
    }

    public ImmutableList<Location> findByCityWithId(String city) {
        final List<Location> locationList = locationRepository.findByLocationAddress_City(city);
        if (locationList != null && !locationList.isEmpty()) {
            return ImmutableList.copyOf(locationList);
        } else {
            throw new NotFoundException("No locations in the city " + city + " was found");
        }
    }

    @Transactional
    public Location create(LocationDto dto, long userId) {
        final Business business = businessService.get(userId);

        if (!business.getVerificationStatus().equals(String.valueOf(BusinessVerificationStatusEnum.VERIFIED))) {
            throw new BusinessVerificationException(BusinessVerificationException.Enum.BUSINESS_NOT_VERIFIED);
        }

        final Address address = addressService.create(dto.getAddress());

        final Set<LocationDescriptionItemEnum> locationDescriptionEnumSet = dto.getDescriptions();

        final Set<LocationDescriptionItem> descriptions = locationDescriptionEnumSet.stream()
                .map(locationDescriptionItemEnum -> locationDescriptionItemEnum.value)
                .map(value -> locationDescriptionItemService.getById(value))
                .collect(Collectors.toSet());

        final Location location = LocationMapper.fromDto(dto);

        location.setLocationAddress(address);
        location.setBusiness(business);
        location.setDescriptions(descriptions);
        location.setCreatedAt(LocalDateTime.now());
        location.setModifiedAt(LocalDateTime.now());

        saveLocation(location);

        //!SERVES_FOOD && OUTSIDE_CATERING_AVAILABLE
        if (locationDescriptionEnumSet.contains(LocationDescriptionItemEnum.OUTSIDE_CATERING_AVAILABLE)) {
            addLocationOutsideCateringAvailableAndDontServeFood(location);
        } else {
            saveLocation(location);
        }
        return location;
    }

    public void saveLocation(Location location) {
        log.info("TRYING TO SAVE LOCATION " + location.toString());
        locationRepository.save(location);
    }

    @Transactional
    public void addAvailability(List<LocationAvailabilityDto> dtos, long locationId) {
        final Location location = get(locationId);

        final List<LocationAvailability> availabilities = dtos.stream().map(LocationAvailabilityMapper::fromDto).collect(Collectors.toList());

        availabilities.stream()
                .peek(availability -> availability.setStatus(AVAILABLE.toString()))
                .peek(availability -> availability.setLocation(location))
                .forEach(availability -> locationAvailabilityRepository.save(availability));

    }

    public Location getWithAvailability(long locationId, String date) {
        final LocalDate localDate = DateTimeUtil.fromStringToFormattedDate(date);
        final Optional<Location> optionalLocation = locationRepository.getByIdWithAvailability(locationId, date);

        if (optionalLocation.isPresent()) {
            return optionalLocation.get();
        }
        throw new NotFoundException("Location with id " + locationId + " DOES NOT EXIST");
    }

    private void addLocationOutsideCateringAvailableAndDontServeFood(Location location) {
        final String locationCity = location.getLocationAddress().getCity();
        final List<Catering> cateringList = cateringRepository.findByCateringAddress_City(locationCity);

        for (Catering catering : cateringList) {
            log.info("CATERING ID " + catering.getId() + ", LOCATION ID " + location.getId());

            catering.addLocation(location);
            catering.setModifiedAt(LocalDateTime.now());

            cateringRepository.save(catering);
        }

        location.setCaterings(new HashSet<>(cateringList));
        saveLocation(location);
    }

    @Transactional
    public Location edit(LocationDto dto, long id) {
        final Location location = get(id);

        location.setEmail(dto.getEmail());
        location.setName(dto.getName());
        location.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()));
        location.setDescription(dto.getDescription());
        location.setSeatingCapacity(dto.getSeatingCapacity());
        location.setStandingCapacity(dto.getStandingCapacity());
        location.setDailyRentCost(Converter.convertPriceString(dto.getDailyRentCost()));

        final Set<LocationDescriptionItemEnum> descriptionsEnum = dto.getDescriptions();

        final Set<LocationDescriptionItem> locationDescription = location.getDescriptions();

        for (LocationDescriptionItemEnum descriptionEnum : descriptionsEnum) {
            if (!locationDescription.contains(descriptionEnum.getValue())) {
                location.addDescriptionItem(locationDescriptionItemService.getByName(descriptionEnum));
            }
        }

        for (LocationDescriptionItem locationDescriptionItem : locationDescription) {
            final Set<String> enumsAsString = descriptionsEnum.stream().map(LocationDescriptionItemEnum::getValue).collect(Collectors.toSet());

            if (!enumsAsString.contains(locationDescriptionItem.getId())) {
                location.removeDescriptionItem(locationDescriptionItem);
            }
        }

        location.setModifiedAt(LocalDateTime.now());

        saveLocation(location);

        return location;

    }

    //TODO: FINISH
    public void delete(long id) {
        //cannot delete when there are reservations pending

        //remove location description
        //remove caterings
        //delete address
        //set deletedAt
    }

    public void modifyAvailabilityAfterBooking(Location location, EventBookDateDto details) {

        final Set<LocationAvailability> locationAvailability = location.getLocationAvailability();

        final LocalDate date = DateTimeUtil.fromStringToFormattedDate(details.getDate());
        final LocalDateTime timeFrom = DateTimeUtil.fromStringToFormattedDateTime(details.getStartTime());
        final LocalDateTime timeTo = DateTimeUtil.fromStringToFormattedDateTime(details.getEndTime());

        final List<LocationAvailability> availabilityForDate = locationAvailability.stream()
                .filter(availability -> availability.getDate().equals(date))
                .filter(availability -> (timeFrom.isEqual(availability.getTimeFrom()) || timeFrom.isAfter(availability.getTimeFrom()))
                        && (timeTo.isEqual(availability.getTimeTo()) || timeTo.isBefore(availability.getTimeTo()))
                        && (availability.getStatus().equals(AVAILABLE.toString())))
                .collect(Collectors.toList());

        final LocationAvailability availability = availabilityForDate.get(0);

        final List<LocationAvailability> modified = modify(availability, date, timeFrom, timeTo);

        locationAvailabilityRepository.delete(availability);

        modified.forEach(modifiedAvailability -> locationAvailabilityRepository.saveAndFlush(modifiedAvailability));

    }

    private List<LocationAvailability> modify(LocationAvailability availability, LocalDate bookingDate, LocalDateTime bookingTimeFrom, LocalDateTime bookingTimeTo) {

        final List<LocationAvailability> modified = new ArrayList<>();

        modified.add(LocationAvailability.builder()
                .status(NOT_AVAILABLE.toString())
                .date(bookingDate)
                .timeFrom(bookingTimeFrom)
                .timeTo(bookingTimeTo)
                .location(availability.getLocation())
                .build());

        if (availability.getTimeFrom().isBefore(bookingTimeFrom)) {
            modified.add(LocationAvailability.builder()
                    .status(AVAILABLE.toString())
                    .date(bookingDate)
                    .timeFrom(availability.getTimeFrom())
                    .timeTo(bookingTimeFrom)
                    .location(availability.getLocation())
                    .build());
        }

        if (availability.getTimeTo().isAfter(bookingTimeTo)) {
            modified.add(LocationAvailability.builder()
                    .status(AVAILABLE.toString())
                    .date(bookingDate)
                    .timeFrom(bookingTimeTo)
                    .timeTo(availability.getTimeTo())
                    .location(availability.getLocation())
                    .build());
        }

        return modified;
    }

}
