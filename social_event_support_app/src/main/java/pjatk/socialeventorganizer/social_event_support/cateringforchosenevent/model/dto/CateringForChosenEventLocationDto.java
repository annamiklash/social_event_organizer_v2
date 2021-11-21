package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto.LocationForEventDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringForChosenEventLocationDto {


    private long id;

    private String dateTime;

    private CateringDto catering;

    private LocationForEventDto eventLocation;
}
