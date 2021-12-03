package pjatk.socialeventorganizer.social_event_support.event.model.dto.initial_booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventBookDateDto {

    @NotNull
    private String startDateTime;

    @NotNull
    private String endDateTime;

    @NotNull
    private int guests;

    private LocationDto locationDto;
}
