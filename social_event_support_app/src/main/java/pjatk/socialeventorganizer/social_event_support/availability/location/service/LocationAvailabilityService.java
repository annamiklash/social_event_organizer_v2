package pjatk.socialeventorganizer.social_event_support.availability.location.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.availability.location.model.LocationAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.location.repository.LocationAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.availability.validator.AvailabilityDatesValidator;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.availability.AvailabilityEnum.AVAILABLE;

@Service
@AllArgsConstructor
@Slf4j
public class LocationAvailabilityService {

    private final LocationAvailabilityRepository locationAvailabilityRepository;

    private final LocationService locationService;

    public List<LocationAvailability> findAllByLocationIdAndDate(long locId, String date) {
        final Optional<List<LocationAvailability>> optionalList = locationAvailabilityRepository.findAvailabilitiesByLocationIdAndDate(locId, date);

        if (optionalList.isPresent()) {
            return optionalList.get();
        }
        throw new NotFoundException("No date " + date + " for location " + locId);
    }

    @Transactional
    public List<LocationAvailability> create(List<AvailabilityDto> dtos, long locationId) {
        final Location location = locationService.get(locationId);

        return dtos.stream()
                .peek(dto ->  AvailabilityDatesValidator.validate(dto.getTimeFrom(),dto.getTimeTo()))
                .map(AvailabilityMapper::fromDtoToLocationAvailability)
                .peek(availability -> availability.setStatus(AVAILABLE.toString()))
                .peek(availability -> availability.setLocation(location))
                .peek(this::save)
                .collect(Collectors.toList());

    }

    private void save(LocationAvailability locationAvailability) {
        locationAvailabilityRepository.save(locationAvailability);
    }
}
