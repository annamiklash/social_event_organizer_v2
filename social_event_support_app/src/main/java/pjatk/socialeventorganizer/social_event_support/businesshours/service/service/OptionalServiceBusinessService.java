package pjatk.socialeventorganizer.social_event_support.businesshours.service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.businesshours.BusinessHoursValidator;
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto;
import pjatk.socialeventorganizer.social_event_support.businesshours.mapper.BusinessHoursMapper;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.model.OptionalServiceBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.repository.OptionalServiceBusinessHoursRepository;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceBusinessService {

    private OptionalServiceBusinessHoursRepository optionalServiceBusinessHoursRepository;

    public List<OptionalServiceBusinessHours> create(List<BusinessHoursDto> dtos) {

        BusinessHoursValidator.validate(dtos);

        return dtos.stream()
                .map(BusinessHoursMapper::fromDtoToOptionalService)
                .peek(this::save)
                .collect(Collectors.toList());
    }

    public OptionalServiceBusinessHours edit(long id, BusinessHoursDto dto) {
        final OptionalServiceBusinessHours businessHours = get(id);

        businessHours.setDay(dto.getDay().name());
        businessHours.setDay(dto.getDay().name());
        businessHours.setTimeTo(DateTimeUtil.toLocalTimeFromTimeString(dto.getTimeTo()));

        save(businessHours);
        return businessHours;

    }

    public OptionalServiceBusinessHours get(long id) {
        return optionalServiceBusinessHoursRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No business hours with id " + id));
    }

    private void save(OptionalServiceBusinessHours optionalServiceBusinessHours) {
        optionalServiceBusinessHoursRepository.save(optionalServiceBusinessHours);
    }

    public void delete(OptionalServiceBusinessHours businessHours) {
        optionalServiceBusinessHoursRepository.delete(businessHours);
    }

}
