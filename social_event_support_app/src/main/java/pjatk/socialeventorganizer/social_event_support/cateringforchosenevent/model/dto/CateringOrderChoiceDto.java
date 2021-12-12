package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringItemDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringOrderChoiceDto implements Serializable {

    private long id;

    @Min(1)
    @NotBlank(message = "Amount is mandatory")
    private int amount;

    private CateringItemDto item;

    private CateringForChosenEventLocationDto catering;
}
