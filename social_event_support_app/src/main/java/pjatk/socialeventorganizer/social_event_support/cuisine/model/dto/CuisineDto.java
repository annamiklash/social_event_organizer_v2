package pjatk.socialeventorganizer.social_event_support.cuisine.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CuisineDto implements Serializable {

    private Long id;

    @NotNull
    private String name;
}
