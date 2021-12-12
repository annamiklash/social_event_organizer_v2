package pjatk.socialeventorganizer.social_event_support.location.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationImageDto {

    @NotNull
    List<ImageDto> details;

    @NotNull
    Integer locationId;
}
