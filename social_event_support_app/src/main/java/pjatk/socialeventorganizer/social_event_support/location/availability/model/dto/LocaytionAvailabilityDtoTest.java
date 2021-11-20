package pjatk.socialeventorganizer.social_event_support.location.availability.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocaytionAvailabilityDtoTest {

    @NotNull
    private String date;

    @NotNull
    private List<TimeAvailability> timeAvailability;
}
