package pjatk.socialeventorganizer.social_event_support.availability.optionalservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository.OptionalServiceAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;

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
public class OptionalServiceAvailabilityService {

    private final OptionalServiceAvailabilityRepository optionalServiceAvailabilityRepository;

    private final OptionalServiceService optionalServiceService;

    public List<OptionalServiceAvailability> findAllByServiceIdAndDate(long id, String date) {

        return optionalServiceAvailabilityRepository.findAvailabilitiesByServiceIdAndDate(id, date);
    }

    public List<OptionalServiceAvailability> create(List<AvailabilityDto> dtos, long serviceId) {
        final OptionalService optionalService = optionalServiceService.get(serviceId);
        final Map<String, List<AvailabilityDto>> byDate = dtos.stream().collect(Collectors.groupingBy(AvailabilityDto::getDate));

        final List<AvailabilityDto> availabilityDtos = byDate.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        final List<OptionalServiceAvailability> result = new ArrayList<>();

        for (AvailabilityDto availabilityDto : availabilityDtos) {
            final OptionalServiceAvailability availability = resolveAvailabilitiesForDay(availabilityDto, optionalService);

            availability.setOptionalService(optionalService);
            availability.setStatus(AVAILABLE.name());

            save(availability);
            result.add(availability);
        }
        return result;
    }

    private OptionalServiceAvailability resolveAvailabilitiesForDay(AvailabilityDto availabilityDto, OptionalService optionalService) {
        final List<OptionalServiceAvailability> available = findAllByServiceIdAndDate(optionalService.getId(), availabilityDto.getDate()).stream()
                .filter(locationAvailability -> "AVAILABLE".equals(locationAvailability.getStatus()))
                .collect(Collectors.toList());

        final List<OptionalServiceAvailability> not_available = findAllByServiceIdAndDate(optionalService.getId(), availabilityDto.getDate()).stream()
                .filter(serviceAvailability -> "NOT_AVAILABLE".equals(serviceAvailability.getStatus()))
                .collect(Collectors.toList());

        if (available.size() == 0 && not_available.size() == 0) {
            return AvailabilityMapper.fromDtoToOptionalServiceAvailability(availabilityDto);
        }

        if (available.size() > 0 && not_available.size() == 0) {
            final Optional<OptionalServiceAvailability> upperBordering = findByServiceIdAndTimeTo(availabilityDto.getTimeFrom(), optionalService.getId()); //upper
            final Optional<OptionalServiceAvailability> lowerBordering = findByServiceIdAndTimeFrom(availabilityDto.getTimeTo(), optionalService.getId());//lower

            if (upperBordering.isEmpty() && lowerBordering.isEmpty()) { // -> save as it is
                return AvailabilityMapper.fromDtoToOptionalServiceAvailability(availabilityDto);
            }

            if (upperBordering.isPresent() && lowerBordering.isEmpty()) { // -> extend upper (upper.setTimeTo(dto.getTimeTo))
                final OptionalServiceAvailability availability = upperBordering.get();
                availability.setTimeTo(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(availabilityDto.getDate(), availabilityDto.getTimeTo())));
                return availability;
            }

            if (upperBordering.isEmpty()) { // -> extend lower (lower.setTimeFrom(dto.getTimeFrom))
                final OptionalServiceAvailability availability = lowerBordering.get();
                availability.setTimeFrom(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(availabilityDto.getDate(), availabilityDto.getTimeFrom())));
                return availability;
            } else {                        // -> change one of them, delete the other,
                final OptionalServiceAvailability lower = upperBordering.get();
                final OptionalServiceAvailability upper = upperBordering.get();

                upper.setTimeTo(lower.getTimeTo());
                deleteById(lower.getId());
                return upper;
            }
        } else {
            if (isNewWithinNotAvailable(availabilityDto, not_available)) {
                throw new IllegalArgumentException("Time from and time to for availability is withing reserved time frame");
            } else {
                return AvailabilityMapper.fromDtoToOptionalServiceAvailability(availabilityDto);
            }
        }
    }

    private boolean isNewWithinNotAvailable(AvailabilityDto availabilityDto, List<OptionalServiceAvailability> not_available) {

        final LocalDateTime from = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(availabilityDto.getDate(), availabilityDto.getTimeFrom()));
        final LocalDateTime to = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(availabilityDto.getDate(), availabilityDto.getTimeTo()));

        for (OptionalServiceAvailability availability : not_available) {
            if (from.isBefore(availability.getTimeTo()) && from.isAfter(availability.getTimeFrom())) {
                return true;
            }
            if (to.isBefore(availability.getTimeTo()) && to.isAfter(availability.getTimeFrom())) {
                return true;
            }
        }
        return false;
    }

    private Optional<OptionalServiceAvailability> findByServiceIdAndTimeFrom(String timeTo, Long serviceId) {
        return optionalServiceAvailabilityRepository.findByServiceIdAndTimeFrom(serviceId, timeTo);

    }

    private Optional<OptionalServiceAvailability> findByServiceIdAndTimeTo(String timeFrom, Long serviceId) {
        return optionalServiceAvailabilityRepository.findByServiceIdAndTimeTo(serviceId, timeFrom);

    }

    public List<OptionalServiceAvailability> edit(List<AvailabilityDto> dtos, long serviceId) {
        final Map<String, List<AvailabilityDto>> byDate = dtos.stream().collect(Collectors.groupingBy(AvailabilityDto::getDate));

        final List<AvailabilityDto> availabilityDtoList = byDate.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        final List<OptionalServiceAvailability> result = new ArrayList<>();

        for (AvailabilityDto dto : availabilityDtoList) {
            optionalServiceAvailabilityRepository.findAvailabilitiesByServiceIdAndDate(serviceId, dto.getDate())
                    .stream()
                    .filter(serviceAvailavbilitie -> serviceAvailavbilitie.getStatus().equals(AVAILABLE.name()))
                    .forEach(cateringAvailability -> deleteById(cateringAvailability.getId()));
            result.add(createOne(dto, serviceId));
        }

        return result;
    }

    private void save(OptionalServiceAvailability optionalServiceAvailability) {
        optionalServiceAvailabilityRepository.save(optionalServiceAvailability);
    }

    public OptionalServiceAvailability createOne(AvailabilityDto dto, long serviceId) {
        final OptionalService service = optionalServiceService.get(serviceId);
        final OptionalServiceAvailability availability = AvailabilityMapper.fromDtoToOptionalServiceAvailability(dto);
        availability.setOptionalService(service);

        save(availability);

        return availability;
    }

    private boolean calendarEmptyForDate(String date, long locationId) {
        List<OptionalServiceAvailability> result = optionalServiceAvailabilityRepository.findByDate(locationId, date);
        return !CollectionUtils.isEmpty(result);
    }

    private void deleteById(long id) {
        optionalServiceAvailabilityRepository.deleteById(id);
    }


}
