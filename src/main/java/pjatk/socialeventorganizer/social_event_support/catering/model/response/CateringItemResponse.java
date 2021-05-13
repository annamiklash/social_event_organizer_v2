package pjatk.socialeventorganizer.social_event_support.catering.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringItemResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    private String type;
}
