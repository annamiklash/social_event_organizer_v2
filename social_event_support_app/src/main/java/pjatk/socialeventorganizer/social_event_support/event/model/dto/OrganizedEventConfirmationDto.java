package pjatk.socialeventorganizer.social_event_support.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto.LocationForEventDto;

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
}
