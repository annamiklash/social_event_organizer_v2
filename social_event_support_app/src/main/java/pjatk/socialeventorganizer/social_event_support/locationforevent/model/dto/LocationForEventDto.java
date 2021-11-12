package pjatk.socialeventorganizer.social_event_support.locationforevent.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationForEventDto {

    private Long id;

    private String dateTimeFrom;

    private String dateTimeTo;

    private Location location;

    private OrganizedEvent event;

//    private Set<CateringForChosenEventLocation> cateringsForEventLocation;
}
