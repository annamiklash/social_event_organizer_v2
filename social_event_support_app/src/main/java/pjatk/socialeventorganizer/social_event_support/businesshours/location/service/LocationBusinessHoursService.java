package pjatk.socialeventorganizer.social_event_support.businesshours.location.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.businesshours.BusinessHoursValidator;
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto;
import pjatk.socialeventorganizer.social_event_support.businesshours.location.model.LocationBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.location.repository.LocationBusinessHoursRepository;
import pjatk.socialeventorganizer.social_event_support.businesshours.mapper.BusinessHoursMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class LocationBusinessHoursService {

    private LocationBusinessHoursRepository locationBusinessHoursRepository;

    public List<LocationBusinessHours> create(List<BusinessHoursDto> dtos) {

        BusinessHoursValidator.validate(dtos);

        return dtos.stream()
                .map(BusinessHoursMapper::fromDtoToLocation)
                .peek(this::save)
                .collect(Collectors.toList());
    }

    private void save(LocationBusinessHours locationBusinessHours) {
        locationBusinessHoursRepository.save(locationBusinessHours);
    }

    public void delete(LocationBusinessHours locationBusinessHours) {
        locationBusinessHoursRepository.delete(locationBusinessHours);
    }

    //TODO: edit method
}
