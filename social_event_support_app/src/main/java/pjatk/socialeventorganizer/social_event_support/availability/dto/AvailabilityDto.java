package pjatk.socialeventorganizer.social_event_support.availability.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailabilityDto implements Serializable {

    private Long id;

    @NotNull
    private String date;

    @NotNull
    private String timeFrom;

    @NotNull
    private String timeTo;

    private String status;

    private LocationDto location;

    private CateringDto catering;

    private OptionalServiceDto optionalService;

}
