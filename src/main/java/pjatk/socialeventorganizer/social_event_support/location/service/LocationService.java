package pjatk.socialeventorganizer.social_event_support.location.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.request.AddressRequest;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringRepository;
import pjatk.socialeventorganizer.social_event_support.enums.LocationDescriptionItemEnum;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.mapper.LocationMapper;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.Location;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDescriptionItem;
import pjatk.socialeventorganizer.social_event_support.location.model.request.LocationDescriptionForFilteringLocationsRequest;
import pjatk.socialeventorganizer.social_event_support.location.model.request.LocationRequest;
import pjatk.socialeventorganizer.social_event_support.location.model.response.LocationInformationResponse;
import pjatk.socialeventorganizer.social_event_support.location.repository.LocationRepository;

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

    CateringRepository cateringRepository;

    public ImmutableList<LocationInformationResponse> findAll() {
        final List<Location> locationList = locationRepository.findAll();

        return locationList.stream()
                .map(location -> locationMapper.mapDTOtoInformationResponse(location))
                .collect(ImmutableList.toImmutableList());

    }

    public ImmutableList<LocationInformationResponse> findByLocationDescription(LocationDescriptionForFilteringLocationsRequest request) {
        final List<LocationDescriptionItemEnum> descriptionItems = request.getDescriptionItems();

        final Set<LocationDescriptionItem> filters = descriptionItems.stream()
                .map(locationDescriptionItemEnum -> locationDescriptionItemEnum.value)
                .map(value -> locationDescriptionItemService.getById(value))
                .collect(Collectors.toSet());

        final List<Location> allLocations = locationRepository.findAll();

        return allLocations.stream()
                .filter(location -> location.getDescriptions().containsAll(filters))
                .map(location -> locationMapper.mapDTOtoInformationResponse(location))
                .collect(ImmutableList.toImmutableList());
    }

    public ImmutableList<LocationInformationResponse> findByName(String name) {
        final List<Location> locationList = locationRepository.findByNameContaining(name);
        if (locationList != null && !locationList.isEmpty()) {
            return locationList.stream()
                    .map(location -> locationMapper.mapDTOtoInformationResponse(location))
                    .collect(ImmutableList.toImmutableList());
        } else {
            throw new NotFoundException("No locations with the name " + name + " was found");
        }
    }

    public ImmutableList<LocationInformationResponse> findByCity(String city) {
        final List<Location> locationList = locationRepository.findByLocationAddress_City(city);
        if (locationList != null && !locationList.isEmpty()) {
            return locationList.stream()
                    .map(location -> locationMapper.mapDTOtoInformationResponse(location))
                    .collect(ImmutableList.toImmutableList());
        } else {
            throw new NotFoundException("No locations in the city " + city + " was found");
        }
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
    public void addNewLocation(LocationRequest locationRequest) {

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
            locationMapper.mapToResponse(saved);
        }
        final Location saved = saveLocation(location);
        locationMapper.mapToResponse(saved);

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
