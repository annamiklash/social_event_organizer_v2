package pjatk.socialeventorganizer.social_event_support.location.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationAvailabilityDto implements Serializable {

    private Long id;

    @NotNull
    private String date;

    @NotNull
    private String timeFrom;

    @NotNull
    private String timeTo;

    private String status;

    private LocationDto location;

}
