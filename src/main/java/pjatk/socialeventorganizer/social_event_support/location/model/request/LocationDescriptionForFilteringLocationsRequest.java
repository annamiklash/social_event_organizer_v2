package pjatk.socialeventorganizer.social_event_support.location.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.enums.LocationDescriptionItemEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDescriptionForFilteringLocationsRequest {

    @NotNull
    List<LocationDescriptionItemEnum> descriptionItems;

}
