package pjatk.socialeventorganizer.social_event_support.availability.optionalservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository.OptionalServiceAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;

import java.util.List;
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

        final Optional<List<OptionalServiceAvailability>> optionalList =
                optionalServiceAvailabilityRepository.findAvailabilitiesByServiceIdAndDate(id, date);

        if (optionalList.isPresent()) {
            return optionalList.get();
        }
        throw new NotFoundException("No available times " + date + " for service " + id);
    }

    public List<OptionalServiceAvailability> create(List<AvailabilityDto> dtos, long id) {
        final OptionalService optionalService = optionalServiceService.get(id);

        return dtos.stream()
                .map(AvailabilityMapper::fromDtoToOptionalServiceAvailability)
                .peek(availability -> availability.setStatus(AVAILABLE.toString()))
                .peek(availability -> availability.setOptionalService(optionalService))
                .peek(this::save)
                .collect(Collectors.toList());
    }

    private void save(OptionalServiceAvailability optionalServiceAvailability) {
        optionalServiceAvailabilityRepository.save(optionalServiceAvailability);
    }
}
