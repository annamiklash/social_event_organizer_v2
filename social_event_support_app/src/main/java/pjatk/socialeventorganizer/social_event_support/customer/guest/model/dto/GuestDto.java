package pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuestDto implements Serializable {

    private long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    private String createdAt;

    private String modifiedAt;

    private CustomerDto customer;

    private OrganizedEventDto organizedEvent;

}
