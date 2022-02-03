package pjatk.socialeventorganizer.social_event_support.businesshours.optionalservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.businesshours.BusinessHoursValidator;
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto;
import pjatk.socialeventorganizer.social_event_support.businesshours.mapper.BusinessHoursMapper;
import pjatk.socialeventorganizer.social_event_support.businesshours.optionalservice.model.OptionalServiceBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.optionalservice.repository.OptionalServiceBusinessHoursRepository;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceBusinessHoursService {

    private final OptionalServiceBusinessHoursRepository optionalServiceBusinessHoursRepository;

    public List<OptionalServiceBusinessHours> create(List<BusinessHoursDto> dtos) {

        BusinessHoursValidator.validate(dtos);

        return dtos.stream()
                .map(BusinessHoursMapper::fromDtoToOptionalService)
                .peek(optionalServiceBusinessHoursRepository::save)
                .collect(Collectors.toList());
    }

    public OptionalServiceBusinessHours edit(long id, BusinessHoursDto dto) {
        final OptionalServiceBusinessHours businessHours = get(id);

        businessHours.setDay(dto.getDay().name());
        businessHours.setDay(dto.getDay().name());
        businessHours.setTimeTo(DateTimeUtil.fromTimeStringToLocalTime(dto.getTimeTo()));

        optionalServiceBusinessHoursRepository.save(businessHours);
        return businessHours;

    }

    public void delete(OptionalServiceBusinessHours businessHours) {
        optionalServiceBusinessHoursRepository.delete(businessHours);
    }

    private OptionalServiceBusinessHours get(long id) {
        return optionalServiceBusinessHoursRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No business hours with id " + id));
    }

}
