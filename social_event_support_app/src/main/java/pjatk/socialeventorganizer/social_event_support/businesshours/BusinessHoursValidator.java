package pjatk.socialeventorganizer.social_event_support.businesshours;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;

import java.util.List;

@UtilityClass
public class BusinessHoursValidator {

    public void validate(List<BusinessHoursDto> dtos) {
        final long daysCount = dtos.stream().map(BusinessHoursDto::getDay).distinct().count();

        if (daysCount != dtos.size()) {
            throw new IllegalArgumentException("Invalid business hours");
        }
    }
}
