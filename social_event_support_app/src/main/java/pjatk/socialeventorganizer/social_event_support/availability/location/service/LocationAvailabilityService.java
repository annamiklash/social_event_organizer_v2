package pjatk.socialeventorganizer.social_event_support.availability.location.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.availability.location.model.LocationAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.location.repository.LocationAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        return locationAvailabilityRepository.findAvailabilitiesByLocationIdAndDate(locId, date);
    }

    @Transactional(rollbackOn = Exception.class)
    public List<LocationAvailability> create(List<AvailabilityDto> dtos, long locationId) {
        final Location location = locationService.get(locationId);
        final Map<String, List<AvailabilityDto>> byDate = dtos.stream().collect(Collectors.groupingBy(AvailabilityDto::getDate));

        final List<AvailabilityDto> availabilityDtos = byDate.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        final List<LocationAvailability> result = new ArrayList<>();

        for (AvailabilityDto availabilityDto : availabilityDtos) {
            final LocationAvailability availability = resolveAvailabilitiesForDay(availabilityDto, location);

            availability.setLocation(location);
            availability.setStatus(AVAILABLE.name());

            save(availability);
            result.add(availability);
        }
        return result;
    }


    public LocationAvailability createOne(AvailabilityDto dto, long locationId) {
        final Location location = locationService.get(locationId);
        final LocationAvailability availability = AvailabilityMapper.fromDtoToLocationAvailability(dto);
        availability.setLocation(location);
        availability.setStatus(AVAILABLE.name());

        save(availability);

        return availability;
    }

    private LocationAvailability resolveAvailabilitiesForDay(AvailabilityDto dto, Location location) {
        final List<LocationAvailability> available = findAllByLocationIdAndDate(location.getId(), dto.getDate()).stream()
                .filter(locationAvailability -> locationAvailability.getStatus().equals("AVAILABLE"))
                .collect(Collectors.toList());

        final List<LocationAvailability> not_available = findAllByLocationIdAndDate(location.getId(), dto.getDate()).stream()
                .filter(locationAvailability -> locationAvailability.getStatus().equals("NOT_AVAILABLE"))
                .collect(Collectors.toList());

        if (available.size() == 0 && not_available.size() == 0) {
            return AvailabilityMapper.fromDtoToLocationAvailability(dto);
        }

        if (available.size() > 0 && not_available.size() == 0) {
            final Optional<LocationAvailability> upperBordering = findByLocationIdAndTimeTo(dto.getTimeFrom(), location.getId()); //upper
            final Optional<LocationAvailability> lowerBordering = findByLocationIdAndTimeFrom(dto.getTimeTo(), location.getId());//lower

            if (upperBordering.isEmpty() && lowerBordering.isEmpty()) { // -> save as it is
                return AvailabilityMapper.fromDtoToLocationAvailability(dto);
            }

            if (upperBordering.isPresent() && lowerBordering.isEmpty()) { // -> extend upper (upper.setTimeTo(dto.getTimeTo))
                final LocationAvailability availability = upperBordering.get();
                availability.setTimeTo(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo())));
                return availability;
            }

            if (upperBordering.isEmpty()) { // -> extend lower (lower.setTimeFrom(dto.getTimeFrom))
                final LocationAvailability availability = lowerBordering.get();
                availability.setTimeFrom(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())));
                return availability;
            } else {                        // -> change one of them, delete the other,
                final LocationAvailability lower = upperBordering.get();
                final LocationAvailability upper = upperBordering.get();

                upper.setTimeTo(lower.getTimeTo());
                deleteById(lower.getId());
                return upper;
            }
        } else {
            if (isNewWithinNotAvailable(dto, not_available)) {
                throw new IllegalArgumentException("Time from and time to for availability is withing reserved time frame");
            } else {
                return AvailabilityMapper.fromDtoToLocationAvailability(dto);
            }
        }
    }

    private boolean isNewWithinNotAvailable(AvailabilityDto dto, List<LocationAvailability> not_available) {
        final LocalDateTime from = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom()));
        final LocalDateTime to = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo()));

        for (LocationAvailability availability : not_available) {
            if (from.isBefore(availability.getTimeTo()) && from.isAfter(availability.getTimeFrom())) {
                return true;
            }
            if (to.isBefore(availability.getTimeTo()) && to.isAfter(availability.getTimeFrom())) {
                return true;
            }
        }
        return false;

    }

    private Optional<LocationAvailability> findByLocationIdAndTimeFrom(String timeTo, long locationId) {
        return locationAvailabilityRepository.findByLocationIdAndTimeFrom(locationId, timeTo);
    }

    private Optional<LocationAvailability> findByLocationIdAndTimeTo(String timeFrom, long locationId) {
        return locationAvailabilityRepository.findByLocationIdAndTimeTo(locationId, timeFrom);
    }

    private boolean calendarEmptyForDate(String date, long locationId) {
        List<LocationAvailability> result = locationAvailabilityRepository.findByDate(locationId, date);
        return CollectionUtils.isEmpty(result);
    }

    public void delete(List<AvailabilityDto> dtos, long locationId) {
        for (AvailabilityDto dto : dtos) {
            locationAvailabilityRepository.findAvailabilitiesByLocationIdAndDate(locationId, dto.getDate()).stream()
                    .filter(cateringAvailability -> cateringAvailability.getStatus().equals(AVAILABLE.name()))
                    .forEach(locationAvailabilityRepository::delete);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public List<LocationAvailability> edit(List<AvailabilityDto> dtos, long locationId) {
        final Map<String, List<AvailabilityDto>> byDate = dtos.stream().collect(Collectors.groupingBy(AvailabilityDto::getDate));

        final List<AvailabilityDto> availabilityDtoList = byDate.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        final List<LocationAvailability> result = new ArrayList<>();

        for (AvailabilityDto dto : availabilityDtoList) {
            locationAvailabilityRepository.findAvailabilitiesByLocationIdAndDate(locationId, dto.getDate())
                    .stream()
                    .filter(cateringAvailability -> cateringAvailability.getStatus().equals(AVAILABLE.name()))
                    .forEach(cateringAvailability -> deleteById(cateringAvailability.getId()));

            result.add(createOne(dto, locationId));
        }

        return result;
    }

    private void save(LocationAvailability locationAvailability) {
        locationAvailabilityRepository.save(locationAvailability);
    }

    private void deleteById(long id) {
        locationAvailabilityRepository.deleteById(id);
        locationAvailabilityRepository.flush();
    }


}
