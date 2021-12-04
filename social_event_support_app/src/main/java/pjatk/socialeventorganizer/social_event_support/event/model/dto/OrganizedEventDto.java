package pjatk.socialeventorganizer.social_event_support.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto.LocationForEventDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizedEventDto implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String date;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    @NotNull
    private int guestCount;

    @NotNull
    private String eventType;

    private String eventStatus;

    private String createdAt;

    private String modifiedAt;

    private String deletedAt;

    private CustomerDto customer;

    private List<GuestDto> guests;

    private LocationForEventDto location;

}
