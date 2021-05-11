package pjatk.socialeventorganizer.social_event_support.location.model.response;

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
public class LocationInformationResponse {

    private Integer id;

    private String name;

    private String description;

    private String email;

    private BigInteger phoneNumber;

    private Integer seatingCapacity;

    private Integer standingCapacity;

    private BigDecimal dailyRentCost;

    private Integer sizeInSqMeters;

    private Address locationAddress;

}
