package pjatk.socialeventorganizer.social_event_support.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto.LocationForEventDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.dto.OptionalServiceForChosenLocationDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizedEventConfirmationDto {

    private Long id;

    private String name;

    private String eventType;

    private boolean isSeated;

    private String eventStatus;

    private LocationForEventDto locationForEvent;

    private List<OptionalServiceForChosenLocationDto> optionalServices;

    private CateringForChosenEventLocationDto catering;
}
