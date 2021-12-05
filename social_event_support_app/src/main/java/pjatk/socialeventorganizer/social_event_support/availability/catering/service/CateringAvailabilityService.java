package pjatk.socialeventorganizer.social_event_support.availability.catering.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.availability.catering.model.CateringAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.catering.repository.CateringAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.availability.validator.AvailabilityDatesValidator;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.availability.AvailabilityEnum.AVAILABLE;

@Service
@AllArgsConstructor
@Slf4j
public class CateringAvailabilityService {

    private final CateringAvailabilityRepository cateringAvailabilityRepository;

    private final CateringService cateringService;

    public List<CateringAvailability> findAllByCateringIdAndDate(long cateringId, String date) {

        final Optional<List<CateringAvailability>> optionalList =
                cateringAvailabilityRepository.findAvailabilitiesByCateringIdAndDate(cateringId, date);

        if (optionalList.isPresent()) {
            return optionalList.get();
        }
        throw new NotFoundException("No date " + date + " for catering " + cateringId);
    }

    public List<CateringAvailability> create(List<AvailabilityDto> dtos, long cateringId) {
        final Catering catering = cateringService.get(cateringId);

        return dtos.stream()
                .peek(dto ->  AvailabilityDatesValidator.validate(dto.getDate(), dto.getTimeFrom(),dto.getTimeTo()))
                .map(AvailabilityMapper::fromDtoToCateringAvailability)
                .peek(availability -> availability.setStatus(AVAILABLE.toString()))
                .peek(availability -> availability.setCatering(catering))
                .peek(this::save)
                .collect(Collectors.toList());

    }

    private void save(CateringAvailability cateringAvailability) {
        cateringAvailabilityRepository.save(cateringAvailability);
    }
}
