package pjatk.socialeventorganizer.social_event_support.customer.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerInformationResponse {

    private String firstName;

    private String lastName;

    private BigInteger phoneNumber;

    private String email;

    private Address address;
}
