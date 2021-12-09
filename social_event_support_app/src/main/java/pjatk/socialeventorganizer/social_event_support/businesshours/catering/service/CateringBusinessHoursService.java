package pjatk.socialeventorganizer.social_event_support.businesshours.catering.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.businesshours.BusinessHoursValidator;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.repository.CateringBusinessHoursRepository;
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto;
import pjatk.socialeventorganizer.social_event_support.businesshours.mapper.BusinessHoursMapper;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CateringBusinessHoursService {

    private final CateringBusinessHoursRepository cateringBusinessHoursRepository;

    public List<CateringBusinessHours> create(List<BusinessHoursDto> dtos) {

        BusinessHoursValidator.validate(dtos);

        return dtos.stream()
                .map(BusinessHoursMapper::fromDtoToCatering)
                .peek(this::save)
                .collect(Collectors.toList());
    }

    private void save(CateringBusinessHours cateringBusinessHours) {
        cateringBusinessHoursRepository.save(cateringBusinessHours);
    }

    public void delete(CateringBusinessHours businessHour) {
        cateringBusinessHoursRepository.delete(businessHour);
    }


    public CateringBusinessHours edit(long id, BusinessHoursDto dto) {
        final CateringBusinessHours businessHours = get(id);

        businessHours.setDay(dto.getDay().name());
        businessHours.setDay(dto.getDay().name());
        businessHours.setTimeTo(DateTimeUtil.toLocalTimeFromTimeString(dto.getTimeTo()));

        save(businessHours);
        return businessHours;

    }

    public CateringBusinessHours get(long id) {
        return cateringBusinessHoursRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No business hours with id " + id));
    }


}
