package pjatk.socialeventorganizer.social_event_support.availability.optionalservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository.OptionalServiceAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.availability.validator.AvailabilityDatesValidator;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotAvailableException;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        for (Map.Entry<String, List<AvailabilityDto>> entry : byDate.entrySet()) {
            if (!calendarEmptyForDate(entry.getKey(), serviceId)) {
                throw new NotAvailableException("There are already some calendar inputs for date " + entry.getKey());
            }
        }

        return dtos.stream()
                .peek(dto -> AvailabilityDatesValidator.validateServiiceAvailability(dto.getDate(), dto.getTimeFrom(), dto.getTimeTo(), findAllByServiceIdAndDate(serviceId, dto.getDate())))
                .map(AvailabilityMapper::fromDtoToOptionalServiceAvailability)
                .peek(availability -> availability.setStatus(AVAILABLE.toString()))
                .peek(availability -> availability.setOptionalService(optionalService))
                .peek(this::save)
                .collect(Collectors.toList());
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
