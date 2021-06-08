package pjatk.socialeventorganizer.social_event_support.optionalService.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import pjatk.socialeventorganizer.social_event_support.address.model.request.AddressRequest;
import pjatk.socialeventorganizer.social_event_support.common.constants.RegexConstants;
import pjatk.socialeventorganizer.social_event_support.enums.LocationDescriptionItemEnum;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionalServiceRequest implements Serializable {

    @NotBlank(message = "Alias is mandatory")
    @Size(min = 1, max = 50, message
            = "The name should be between 1 and 30 characters")
    private String alias;

    @NotBlank(message = "Type is mandatory")
    @Size(min = 1, max = 50, message
            = "The name should be between 1 and 30 characters")
    private String type;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(min = 5, max = 100, message
            = "Email should be between 5 and 100 characters")
    private String email;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 1, max = 300, message
            = "The name should be between 1 and 300 characters")
    private String description;

    @NotBlank(message = "Service cost is mandatory.")
    @Pattern(regexp = RegexConstants.PRICE_REGEX, message = "should contain only digits or digits separated by a dot sign (1.23)")
    private String serviceCost;
}
