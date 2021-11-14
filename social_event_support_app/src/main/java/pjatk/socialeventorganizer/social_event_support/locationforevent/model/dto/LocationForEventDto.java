package pjatk.socialeventorganizer.social_event_support.locationforevent.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationForEventDto {

    private Long id;

    private String timeFrom;

    private String timeTo;

    private int guests;

    private String confirmationStatus;

    private LocationDto location;

    private OrganizedEventDto event;

//    private Set<CateringForChosenEventLocation> cateringsForEventLocation;
}