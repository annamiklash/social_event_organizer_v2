package pjatk.socialeventorganizer.social_event_support.catering.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.request.AddressRequest;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringMapper;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.model.request.CateringRequest;
import pjatk.socialeventorganizer.social_event_support.catering.model.response.CateringInformationResponse;
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CateringService {

    CateringRepository repository;

    CateringMapper cateringMapper;

    LocationService locationService;

    AddressService addressService;

    AddressMapper addressMapper;

    public ImmutableList<CateringInformationResponse> findAll() {
        final List<Catering> cateringList = repository.findAll();
        return cateringList.stream()
                .map(catering -> cateringMapper.mapDTOtoCateringInformationResponse(catering))
                .collect(ImmutableList.toImmutableList());
    }

    public ImmutableList<CateringInformationResponse> findByName(String name) {
        final List<Catering> cateringList = repository.findByNameContaining(name);
        if (cateringList != null && !cateringList.isEmpty()) {
            return cateringList.stream()
                    .map(catering -> cateringMapper.mapDTOtoCateringInformationResponse(catering))
                    .collect(ImmutableList.toImmutableList());
        } else {
            throw new NotFoundException("No catering with the name " + name + " was found");
        }
    }

    public ImmutableList<CateringInformationResponse> findByCity(String city) {
        final List<Catering> cateringList = repository.findByCateringAddress_City(city);
        if (cateringList != null && !cateringList.isEmpty()) {
            return cateringList.stream()
                    .map(catering -> cateringMapper.mapDTOtoCateringInformationResponse(catering))
                    .collect(ImmutableList.toImmutableList());
        } else {
            throw new NotFoundException("No catering in the city " + city + " was found");
        }
    }

    @Transactional
    public void addNewCatering(CateringRequest request) {

        final AddressRequest addressRequest = request.getAddressRequest();
        final Address address = addressMapper.mapToDTO(addressRequest);

        final Catering catering = cateringMapper.mapToDTO(request, address);
        final Catering savedCatering = saveCatering(catering);

        if (request.isOffersOutsideCatering()) {
            addCateringToLocationsWithSameCity(savedCatering);
        } else {
            addCateringToGivenLocation(savedCatering, request.getLocationId());
        }
        cateringMapper.mapToResponse(savedCatering);
    }

    @Transactional
    public void addCateringToGivenLocation(Catering savedCatering, long locationId) {
        final Location location = locationService.findById(locationId);
        location.addCatering(savedCatering);
        locationService.saveLocation(location);
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

    @Transactional
    public void updateCatering(Long cateringId, CateringRequest request, Long addressId) {
        if (!cateringWithIdExists(cateringId)) {
            throw new IllegalArgumentException("Catering with ID " + cateringId + " does not exist");
        }
        final Catering fetchedCatering = getCateringById(cateringId);

        cateringMapper.updateDTO(request, fetchedCatering);
        final Address address = fetchedCatering.getCateringAddress();
        if (!addressService.addressWithIdExists(addressId) || !address.getId().equals(addressId)) {
            throw new IllegalArgumentException("Address with ID " + addressId + " does not exist or does not correspond to catering");
        }
        log.info("TRYING TO UPDATE " + fetchedCatering);
        final AddressRequest addressRequest = request.getAddressRequest();
        final Address updatedAddress = addressMapper.updateMapToDTO(addressRequest, addressId);
        fetchedCatering.setCateringAddress(updatedAddress);
        repository.save(fetchedCatering);
        log.info("UPDATED");
    }

    public void deleteCatering(Long id) {
        if (!cateringWithIdExists(id)) {
            throw new IllegalArgumentException("Catering with ID " + id + " does not exist");
        }
        repository.deleteById(id);
        log.info("TRYING TO DELETE CATERING WITH ID " + id);
    }


    public boolean cateringWithIdExists(Long id) {
        log.info("CHECKING IF CATERING WITH ID " + id + " EXISTS");
        return repository.existsById(id);
    }

    public Catering getCateringById(Long id) {
        log.info("FETCHING CATERING WITH ID " + id);
        final Optional<Catering> optionalCatering = repository.findById(id);
        if (optionalCatering.isPresent()) {
            return optionalCatering.get();
        }
        throw new NotFoundException("No catering with id " + id + " found.");
    }

    public Catering saveCatering(Catering catering) {
        log.info("TRYING TO SAVE" + catering.toString());

        return repository.saveAndFlush(catering);
    }


}
