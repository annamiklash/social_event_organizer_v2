package pjatk.socialeventorganizer.social_event_support.catering.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringInformationResponse {

    private String name;

    private String description;

    private String email;

    private BigInteger phoneNumber;

    private BigDecimal serviceCost;

    private Address cateringAddress;

}
