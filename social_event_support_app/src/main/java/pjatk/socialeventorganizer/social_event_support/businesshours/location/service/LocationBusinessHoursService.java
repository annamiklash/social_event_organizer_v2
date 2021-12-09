package pjatk.socialeventorganizer.social_event_support.businesshours.location.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.businesshours.BusinessHoursValidator;
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto;
import pjatk.socialeventorganizer.social_event_support.businesshours.location.model.LocationBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.location.repository.LocationBusinessHoursRepository;
import pjatk.socialeventorganizer.social_event_support.businesshours.mapper.BusinessHoursMapper;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

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

    public void delete(LocationBusinessHours locationBusinessHours) {
        locationBusinessHoursRepository.delete(locationBusinessHours);
    }

    public LocationBusinessHours edit(long id, BusinessHoursDto dto) {
        final LocationBusinessHours businessHours = get(id);

        businessHours.setDay(dto.getDay().name());
        businessHours.setDay(dto.getDay().name());
        businessHours.setTimeTo(DateTimeUtil.toLocalTimeFromTimeString(dto.getTimeTo()));

        save(businessHours);
        return businessHours;

    }

    public LocationBusinessHours get(long id) {
        return locationBusinessHoursRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No business hours with id " + id));
    }

    private void save(LocationBusinessHours locationBusinessHours) {
        locationBusinessHoursRepository.save(locationBusinessHours);
    }

}
