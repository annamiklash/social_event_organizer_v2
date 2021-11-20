package pjatk.socialeventorganizer.social_event_support.location.availability.model.dto;

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
public class TimeAvailability implements Serializable {

    @NotNull
    private String timeFrom;

    @NotNull
    private String dateTo;

    private String status;
}
