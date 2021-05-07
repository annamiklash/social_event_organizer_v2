package pjatk.socialeventorganizer.social_event_support.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import pjatk.socialeventorganizer.social_event_support.common.RegexConstants;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/*
FIELDS NAMES AND TYPES  MUST MATCH THE KEY VALUES IN JSON BODY.
 */
public class CateringRequest implements Serializable {

    @NotNull
    private AddressRequest addressRequest;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 100, message
            = "The name should be between 1 and 100 characters")
    @Pattern(regexp = RegexConstants.NAME_REGEX)
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(min = 5, max = 100, message
            = "Email should be between 5 and 100 characters")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Size(min = 9, max = 9, message
            = "phone number should be 9 characters long")
    @Pattern(regexp = RegexConstants.PHONE_NUMBER_REGEX, message = "should contain only digits")
    private String phoneNumber;

    @NotBlank(message = "If there no are service cost, please enter 0")
    @Pattern(regexp = RegexConstants.PRICE_REGEX, message = "should contain only digits or digits separated by a dot sign (1.23)")
    private String serviceCost;

    private String description;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer businessId;

    @NotNull
    private Long locationId;

    @NotNull
    private boolean offersOutsideCatering;
}


