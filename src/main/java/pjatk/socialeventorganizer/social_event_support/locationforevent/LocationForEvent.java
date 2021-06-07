package pjatk.socialeventorganizer.social_event_support.locationforevent;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.event.dto.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.Location;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "location_for_event")
public class LocationForEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location_for_event")
    private Long id;

    @Column(name = "time_from")
    private LocalDateTime dateTimeFrom;

    @Column(name = "time_to")
    private LocalDateTime datTimeTo;

    @ManyToOne
    @JoinColumn(name = "id_location", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "id_organized_event", nullable = false)
    private OrganizedEvent event;


}
