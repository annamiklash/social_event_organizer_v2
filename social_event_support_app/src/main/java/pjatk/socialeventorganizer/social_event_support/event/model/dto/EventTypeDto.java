package pjatk.socialeventorganizer.social_event_support.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventTypeDto implements Serializable {

    private Long id;

    private String type;

    private String description;

    private String createdAt;

    private Set<OrganizedEventDto> events;
}
