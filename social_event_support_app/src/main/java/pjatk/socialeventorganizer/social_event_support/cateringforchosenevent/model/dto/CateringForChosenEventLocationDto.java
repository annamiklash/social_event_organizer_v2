package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto.LocationForEventDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringForChosenEventLocationDto {

    private long id;

    @NotNull
    private String time;

    @NotNull
    private String comment;

    private String confirmationStatus;

    private CateringDto catering;

    private LocationForEventDto eventLocation;

    private List<CateringOrderChoiceDto> order;
}
