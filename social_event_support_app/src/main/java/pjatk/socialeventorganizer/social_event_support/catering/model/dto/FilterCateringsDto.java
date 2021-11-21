package pjatk.socialeventorganizer.social_event_support.catering.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterCateringsDto {

    private List<String> cuisines;

    private String priceNotLessThen;

    private String priceNotMoreThan;

    private String keyword;
}
