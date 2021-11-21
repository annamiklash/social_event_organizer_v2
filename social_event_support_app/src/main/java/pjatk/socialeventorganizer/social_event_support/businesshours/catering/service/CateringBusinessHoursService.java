package pjatk.socialeventorganizer.social_event_support.businesshours.catering.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.businesshours.BusinessHoursValidator;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.repository.CateringBusinessHoursRepository;
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto;
import pjatk.socialeventorganizer.social_event_support.businesshours.mapper.BusinessHoursMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CateringBusinessHoursService {

    private CateringBusinessHoursRepository cateringBusinessHoursRepository;

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
}
