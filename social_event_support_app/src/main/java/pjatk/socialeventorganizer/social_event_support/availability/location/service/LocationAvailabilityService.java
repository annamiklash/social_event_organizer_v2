package pjatk.socialeventorganizer.social_event_support.availability.location.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.availability.location.model.LocationAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.location.repository.LocationAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.repository.LocationRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.availability.AvailabilityEnum.AVAILABLE;
import static pjatk.socialeventorganizer.social_event_support.availability.AvailabilityEnum.NOT_AVAILABLE;

@Service
@AllArgsConstructor
@Slf4j
public class LocationAvailabilityService {

    private final LocationAvailabilityRepository locationAvailabilityRepository;

    private final LocationRepository locationRepository;

    @Transactional(rollbackOn = Exception.class)
    public List<LocationAvailability> update(List<AvailabilityDto> dtos, long locationId, boolean deleteAll) {
        final Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Location does not exist"));

        final Map<String, List<AvailabilityDto>> byDate = dtos.stream()
                .collect(Collectors.groupingBy(AvailabilityDto::getDate));

        final List<AvailabilityDto> availabilityDtos = byDate.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        final List<LocationAvailability> result = new ArrayList<>();

        for (AvailabilityDto availabilityDto : availabilityDtos) {
            final LocationAvailability availability = resolveAvailabilitiesForDay(availabilityDto, location, deleteAll);

            availability.setLocation(location);
            availability.setStatus(AVAILABLE.name());

            save(availability);
            result.add(availability);
        }
        return result;
    }

    public List<LocationAvailability> findAllByLocationIdAndDate(long locId, String date) {
        return locationAvailabilityRepository.find(locId, date);
    }


    public void delete(List<AvailabilityDto> availabilityDtos, long locationId) {
        for (AvailabilityDto availabilityDto : availabilityDtos) {
            locationAvailabilityRepository.findWithStatusAvailable(locationId, availabilityDto.getDate())
                    .forEach(locationAvailabilityRepository::delete);
        }
    }

    public LocationAvailability getByDateAndTime(String date, String timeFrom, String timeTo) {
        return locationAvailabilityRepository.getByDateAndTime(date, timeFrom, timeTo)
                .orElseThrow(() -> new NotFoundException("Nothing for given date and time"));

    }

    public void updateToAvailable(LocationAvailability locationAvailability, Location location) {
        final AvailabilityDto availabilityDto = AvailabilityMapper.toDto(locationAvailability);

        final LocationAvailability availability = resolveAvailabilitiesForDay(availabilityDto, location, false);
        availability.setStatus(AVAILABLE.name());
        save(availability);

    }

    private LocationAvailability resolveAvailabilitiesForDay(AvailabilityDto availabilityDto, Location location, boolean deleteAll) {
        final List<LocationAvailability> locationAvailabilityWithStatusAvailableList =
                findAllByLocationIdAndDate(location.getId(), availabilityDto.getDate())
                .stream()
                .filter(locationAvailability -> "AVAILABLE".equals(locationAvailability.getStatus()))
                .collect(Collectors.toList());

        if (deleteAll) {
            for (LocationAvailability locationAvailability : locationAvailabilityWithStatusAvailableList) {
                locationAvailabilityRepository.delete(locationAvailability);
                locationAvailabilityRepository.flush();
            }
        }

        final List<LocationAvailability> notAvailable =
                findAllByLocationIdAndDate(location.getId(), availabilityDto.getDate())
                .stream()
                .filter(locationAvailability -> locationAvailability.getStatus().equals("NOT_AVAILABLE"))
                .collect(Collectors.toList());

        if (locationAvailabilityWithStatusAvailableList.size() == 0 && notAvailable.size() == 0) {
            return AvailabilityMapper.fromDtoToLocationAvailability(availabilityDto);
        }

        if (locationAvailabilityWithStatusAvailableList.size() > 0 || isToCancel(notAvailable, availabilityDto)) {
            final String timeFrom = DateTimeUtil.joinDateAndTime(availabilityDto.getDate(), availabilityDto.getTimeFrom());
            final Optional<LocationAvailability> upperBordering =
                    locationAvailabilityRepository.findByLocationIdAndTimeTo(location.getId(), timeFrom); //upper

            final String timeTo = DateTimeUtil.joinDateAndTime(availabilityDto.getDate(), availabilityDto.getTimeTo());
            final Optional<LocationAvailability> lowerBordering =
                    locationAvailabilityRepository.findByLocationIdAndTimeFrom(location.getId(), timeTo);//lower

            if (upperBordering.isEmpty() && lowerBordering.isEmpty()) { // -> save as it is
                return AvailabilityMapper.fromDtoToLocationAvailability(availabilityDto);
            }

            if (upperBordering.isPresent() && lowerBordering.isEmpty()) { // -> extend upper (upper.setTimeTo(availabilityDto.getTimeTo))
                final LocationAvailability availability = upperBordering.get();
                availability.setTimeTo(DateTimeUtil.fromStringToFormattedDateTime(timeTo));
                return availability;
            }

            if (upperBordering.isEmpty()) { // -> extend lower (lower.setTimeFrom(availabilityDto.getTimeFrom))
                final LocationAvailability availability = lowerBordering.get();
                availability.setTimeFrom(DateTimeUtil.fromStringToFormattedDateTime(timeFrom));
                return availability;
            } else {                        // -> change one of them, delete the other,
                final LocationAvailability lower = upperBordering.get();
                final LocationAvailability upper = upperBordering.get();

                upper.setTimeTo(lower.getTimeTo());
                deleteById(lower.getId());
                return upper;
            }
        } else {
            if (isNewWithinNotAvailable(availabilityDto, notAvailable)) {
                throw new IllegalArgumentException("Time from and time to for availability is withing reserved time frame");
            } else {
                return AvailabilityMapper.fromDtoToLocationAvailability(availabilityDto);
            }
        }
    }

    private boolean isToCancel(List<LocationAvailability> notAvailable, AvailabilityDto dto) {
        final Optional<LocationAvailability> or = notAvailable.stream().filter(locationAvailability -> locationAvailability.getStatus().equals(NOT_AVAILABLE.name())
                && locationAvailability.getDate().equals(DateTimeUtil.fromStringToFormattedDate(dto.getDate()))
                && locationAvailability.getTimeFrom().equals(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())))
                && locationAvailability.getTimeTo().equals(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom()))))
                .findAny();
        return or.isPresent();
    }

    private boolean isToCancel(List<LocationAvailability> notAvailable, AvailabilityDto dto) {
        final Optional<LocationAvailability> or = notAvailable.stream().filter(locationAvailability -> locationAvailability.getStatus().equals(NOT_AVAILABLE.name())
                && locationAvailability.getDate().equals(DateTimeUtil.fromStringToFormattedDate(dto.getDate()))
                && locationAvailability.getTimeFrom().equals(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())))
                && locationAvailability.getTimeTo().equals(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom()))))
                .findAny();
        return or.isPresent();
    }

    private boolean isToCancel(List<LocationAvailability> notAvailable, AvailabilityDto dto) {
        final Optional<LocationAvailability> or = notAvailable.stream().filter(locationAvailability -> locationAvailability.getStatus().equals(NOT_AVAILABLE.name())
                && locationAvailability.getDate().equals(DateTimeUtil.fromStringToFormattedDate(dto.getDate()))
                && locationAvailability.getTimeFrom().equals(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())))
                && locationAvailability.getTimeTo().equals(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom()))))
                .findAny();
        return or.isPresent();
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

    private void save(LocationAvailability locationAvailability) {
        locationAvailabilityRepository.save(locationAvailability);
    }

    private void deleteById(long id) {
        locationAvailabilityRepository.deleteById(id);
        locationAvailabilityRepository.flush();
    }


}
