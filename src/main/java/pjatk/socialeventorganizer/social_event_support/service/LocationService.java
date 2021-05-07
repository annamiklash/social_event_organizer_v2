package pjatk.socialeventorganizer.social_event_support.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.mapper.LocationMapper;
import pjatk.socialeventorganizer.social_event_support.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.model.dto.Catering;
import pjatk.socialeventorganizer.social_event_support.model.dto.Location;
import pjatk.socialeventorganizer.social_event_support.model.dto.LocationDescriptionItem;
import pjatk.socialeventorganizer.social_event_support.model.enums.LocationDescriptionItemEnum;
import pjatk.socialeventorganizer.social_event_support.model.exception.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.model.request.AddressRequest;
import pjatk.socialeventorganizer.social_event_support.model.request.LocationDescriptionRequestForFilteringLocations;
import pjatk.socialeventorganizer.social_event_support.model.request.LocationRequest;
import pjatk.socialeventorganizer.social_event_support.model.response.LocationResponse;
import pjatk.socialeventorganizer.social_event_support.repository.CateringRepository;
import pjatk.socialeventorganizer.social_event_support.repository.LocationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class LocationService {

    LocationRepository locationRepository;

    LocationDescriptionItemService locationDescriptionItemService;

    AddressMapper addressMapper;

    LocationMapper locationMapper;

    //    CateringService cateringService;
    CateringRepository cateringRepository;

    public ImmutableList<Location> findAll() {
        final List<Location> locationList = locationRepository.findAll();
        return ImmutableList.copyOf(locationList);
    }

    public ImmutableList<Location> findByLocationDescription(LocationDescriptionRequestForFilteringLocations request) {
        final List<LocationDescriptionItemEnum> descriptionItems = request.getDescriptionItems();
        final List<String> values = descriptionItems.stream()
                .map(locationDescriptionItemEnum -> locationDescriptionItemEnum.value)
                .collect(Collectors.toList());

        final List<Integer> locationIdList = locationRepository.filterByDescriptions(values, values.size());
        final List<Location> collect = locationIdList.stream()
                .map(integer -> locationRepository.findById((long) integer))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return ImmutableList.copyOf(collect);
    }

    public ImmutableList<Location> findByName(String name) {
        final List<Location> locationList = locationRepository.findByNameContaining(name);
        if (locationList != null && !locationList.isEmpty()) {
            return ImmutableList.copyOf(locationList);
        } else {
            throw new NotFoundException("No locations with the name " + name + " was found");
        }
    }

    public ImmutableList<Location> findByCity(String city) {
        final List<Location> locationList = locationRepository.findByLocationAddress_City(city);
        if (locationList != null && !locationList.isEmpty()) {
            return ImmutableList.copyOf(locationList);
        } else {
            throw new NotFoundException("No locations in the city " + city + " was found");
        }
    }

    @Transactional
    public LocationResponse addNewLocation(LocationRequest locationRequest) {

        final AddressRequest addressRequest = locationRequest.getAddressRequest();
        final Address address = addressMapper.mapToDTO(addressRequest);
        final Set<LocationDescriptionItemEnum> locationDescriptionEnumSet = locationRequest.getLocationDescription();

        final Set<LocationDescriptionItem> descriptions = locationDescriptionEnumSet.stream()
                .map(locationDescriptionItemEnum -> locationDescriptionItemEnum.value)
                .map(value -> locationDescriptionItemService.getById(value))
                .collect(Collectors.toSet());

        final Location location = locationMapper.mapToDTO(locationRequest, address);
        location.setDescriptions(descriptions);

        //!SERVES_FOOD && OUTSIDE_CATERING_AVAILABLE
        if (!locationDescriptionEnumSet.contains(LocationDescriptionItemEnum.SERVES_FOOD) && locationDescriptionEnumSet.contains(LocationDescriptionItemEnum.OUTSIDE_CATERING_AVAILABLE)) {
            final Location saved = addLocationOutsideCateringAvailableAndDontServeFood(location);
            return locationMapper.mapToResponse(saved);
        }
        final Location saved = saveLocation(location);
        return locationMapper.mapToResponse(saved);

    }

    @Transactional
    Location addLocationOutsideCateringAvailableAndDontServeFood(Location location) {
        final Location savedLocation = saveLocation(location);

        final String locationCity = savedLocation.getLocationAddress().getCity();
        final List<Catering> cateringList = cateringRepository.findByCateringAddress_City(locationCity);

        for (Catering catering : cateringList) {
            log.info("CATERING ID " + catering.getId() + ", LOCATION ID " + savedLocation.getId());
            catering.addLocation(savedLocation);
            cateringRepository.save(catering);
        }
        return savedLocation;
    }

    public Location saveLocation(Location location) {
        log.info("TRYING TO SAVE LOCATION " + location.toString());
        return locationRepository.save(location);
    }

    public Location findById(long id) {
        final Optional<Location> optionalLocation = locationRepository.findById(id);
        if (optionalLocation.isPresent()) {
            return optionalLocation.get();
        }
        throw new NotFoundException("No location with id " + id);
    }

}
