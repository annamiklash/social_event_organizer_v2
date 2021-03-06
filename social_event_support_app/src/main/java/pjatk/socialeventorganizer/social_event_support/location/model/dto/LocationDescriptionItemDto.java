package pjatk.socialeventorganizer.social_event_support.location.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDescriptionItemDto {

    @NotNull
    private String id;

    @NotNull
    private String description;

    private Set<LocationDto> locations;
}
