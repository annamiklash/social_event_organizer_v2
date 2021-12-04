package pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.dto.OptionalServiceForChosenLocationDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationForEventDto {

    private Long id;

    @NotNull
    private String timeFrom;

    @NotNull
    private String timeTo;

    private int guestsCount;

    private String confirmationStatus;

    private LocationDto location;

    private OrganizedEventDto event;

    private List<CateringForChosenEventLocationDto> caterings;

    private List<OptionalServiceForChosenLocationDto> optionalServices;
}
