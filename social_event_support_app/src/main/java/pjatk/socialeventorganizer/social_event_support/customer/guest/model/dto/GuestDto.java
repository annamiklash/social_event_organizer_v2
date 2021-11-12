package pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuestDto implements Serializable {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String createdAt;

    private String modifiedAt;

    private Integer customerId;

    private CustomerDto customer;

}
