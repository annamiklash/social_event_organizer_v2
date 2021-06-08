package pjatk.socialeventorganizer.social_event_support.optionalService.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionalServiceInformationResponse {

    private Integer id;

    private String alias;

    private String description;

    private String type;

    private String email;

    private BigDecimal serviceCost;

}
