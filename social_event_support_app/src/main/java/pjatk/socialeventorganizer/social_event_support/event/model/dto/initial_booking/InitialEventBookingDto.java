package pjatk.socialeventorganizer.social_event_support.event.model.dto.initial_booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitialEventBookingDto {

    private long id;

    @NotNull
    private String name;

    @NotNull
    private String eventType;

    @NotNull
    private EventBookDateDto details;

    private boolean isSeated;

    private String eventStatus;


}
