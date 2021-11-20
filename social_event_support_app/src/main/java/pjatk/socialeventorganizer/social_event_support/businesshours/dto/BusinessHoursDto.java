package pjatk.socialeventorganizer.social_event_support.businesshours.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.businesshours.DayEnum;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessHoursDto implements Serializable {

    private long id;

    @NotNull
    private DayEnum day;

    @NotNull
    private String timeFrom;

    @NotNull
    private String timeTo;

    private LocationDto location;

    private CateringDto catering;

    private OptionalServiceDto optionalService;
}
