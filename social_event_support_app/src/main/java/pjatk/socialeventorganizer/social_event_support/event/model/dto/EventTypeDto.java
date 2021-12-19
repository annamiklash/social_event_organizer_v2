package pjatk.socialeventorganizer.social_event_support.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventTypeDto implements Serializable {

    private Long id;

    @NotNull
    private String type;

    private Set<OrganizedEventDto> events;
}
