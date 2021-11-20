package pjatk.socialeventorganizer.social_event_support.location.availability.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.availability.model.LocationAvailability;
import pjatk.socialeventorganizer.social_event_support.location.availability.repository.LocationAvailabilityRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class LocationAvailabilityService {

    private final LocationAvailabilityRepository locationAvailabilityRepository;

    public List<LocationAvailability> findAllByLocationIdAndDate(long locId, String date) {
        final Optional<List<LocationAvailability>> optionalList = locationAvailabilityRepository.findAvailabilitiesByLocationIdAndDate(locId, date);

        if (optionalList.isPresent()) {
            return optionalList.get();
        }
        throw new NotFoundException("No date " + date + " for location " + locId);
    }


}
