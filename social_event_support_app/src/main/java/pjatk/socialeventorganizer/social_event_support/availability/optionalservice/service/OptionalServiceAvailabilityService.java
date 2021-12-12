package pjatk.socialeventorganizer.social_event_support.availability.optionalservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository.OptionalServiceAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Optional;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.availability.AvailabilityEnum.AVAILABLE;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceAvailabilityService {

    private final OptionalServiceAvailabilityRepository optionalServiceAvailabilityRepository;

    private final OptionalServiceService optionalServiceService;


    public List<OptionalServiceAvailability> update(List<AvailabilityDto> dtos, long serviceId, boolean deleteAll) {
        final OptionalService optionalService = optionalServiceService.get(serviceId);
        final List<OptionalServiceAvailability> result = new ArrayList<>();

        for (AvailabilityDto availabilityDto : dtos) {
            final OptionalServiceAvailability availability =
                    resolveAvailabilitiesForDay(availabilityDto, optionalService, deleteAll);

            availability.setOptionalService(optionalService);
            availability.setStatus(AVAILABLE.name());

            optionalServiceAvailabilityRepository.save(availability);
            result.add(availability);
        }
        return result;
    }

    public List<OptionalServiceAvailability> findAllByServiceIdAndDate(long id, String date) {
        return optionalServiceAvailabilityRepository.findAvailabilitiesByServiceIdAndDate(id, date);
    }

    public void delete(long locationId, String date) {
        optionalServiceAvailabilityRepository.findAvailabilitiesByServiceIdAndDate(locationId, date).stream()
                .filter(cateringAvailability -> AVAILABLE.name().equals(cateringAvailability.getStatus()))
                .forEach(optionalServiceAvailabilityRepository::delete);
    }

    public void updateToAvailable(OptionalServiceAvailability locationAvailability, OptionalService service) {
        final AvailabilityDto availabilityDto = AvailabilityMapper.toDto(locationAvailability);

        final OptionalServiceAvailability availability = resolveAvailabilitiesForDay(availabilityDto, service, false);
        availability.setStatus(AVAILABLE.name());
        optionalServiceAvailabilityRepository.save(availability);

    }

    public OptionalServiceAvailability getByDateAndTime(String date, String timeFrom, String timeTo) {
        return optionalServiceAvailabilityRepository.getByDateAndTime(date, timeFrom, timeTo)
                .orElseThrow(() -> new NotFoundException("Nothing for given date and time"));
    }

    private OptionalServiceAvailability resolveAvailabilitiesForDay(AvailabilityDto dto, OptionalService service, boolean deleteAll) {
        final List<OptionalServiceAvailability> available = findAllByServiceIdAndDate(service.getId(), dto.getDate()).stream()
                .filter(serviceAvailability -> "AVAILABLE".equals(serviceAvailability.getStatus()))
                .collect(Collectors.toList());

        if (deleteAll) {
            available.forEach(optionalServiceAvailabilityRepository::delete);
        }

        final List<OptionalServiceAvailability> notAvailable = findAllByServiceIdAndDate(service.getId(), dto.getDate()).stream()
                .filter(locationAvailability -> "NOT_AVAILABLE".equals(locationAvailability.getStatus()))
                .collect(Collectors.toList());

        if (available.size() == 0 && notAvailable.size() == 0) {
            return AvailabilityMapper.fromDtoToOptionalServiceAvailability(dto);
        }
        if (available.size() > 0 && notAvailable.size() == 0) {
            final Optional<OptionalServiceAvailability> upperBordering = findByLocationIdAndTimeTo(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom()), service.getId()); //upper
            final Optional<OptionalServiceAvailability> lowerBordering = findByLocationIdAndTimeFrom(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo()), service.getId());//lower

            if (upperBordering.isEmpty() && lowerBordering.isEmpty()) { // -> save as it is
                return AvailabilityMapper.fromDtoToOptionalServiceAvailability(dto);
            }

            if (upperBordering.isPresent() && lowerBordering.isEmpty()) { // -> extend upper (upper.setTimeTo(dto.getTimeTo))
                final OptionalServiceAvailability availability = upperBordering.get();
                availability.setTimeTo(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo())));
                return availability;
            }

            if (upperBordering.isEmpty()) { // -> extend lower (lower.setTimeFrom(dto.getTimeFrom))
                final OptionalServiceAvailability availability = lowerBordering.get();
                availability.setTimeFrom(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())));
                return availability;
            } else {                        // -> change one of them, delete the other,
                final OptionalServiceAvailability lower = upperBordering.get();
                final OptionalServiceAvailability upper = upperBordering.get();

                upper.setTimeTo(lower.getTimeTo());
                optionalServiceAvailabilityRepository.deleteById(lower.getId());
                return upper;
            }
        } else {
            if (isNewWithinNotAvailable(dto, notAvailable)) {
                throw new IllegalArgumentException("Time from and time to for availability is withing reserved time frame");
            } else {
                return AvailabilityMapper.fromDtoToOptionalServiceAvailability(dto);
            }
        }
    }

    private boolean isNewWithinNotAvailable(AvailabilityDto dto, List<OptionalServiceAvailability> notAvailable) {
        final LocalDateTime from = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom()));
        final LocalDateTime to = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo()));

        for (OptionalServiceAvailability availability : notAvailable) {
            if (from.isBefore(availability.getTimeTo()) && from.isAfter(availability.getTimeFrom())) {
                return true;
            }
            if (to.isBefore(availability.getTimeTo()) && to.isAfter(availability.getTimeFrom())) {
                return true;
            }
        }
        return false;

    }

}
