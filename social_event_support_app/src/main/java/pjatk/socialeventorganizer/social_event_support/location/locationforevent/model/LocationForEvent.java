package pjatk.socialeventorganizer.social_event_support.location.locationforevent.model;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
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
    private LocalDateTime dateTimeTo;

    @Min(1)
    @Column(name = "guests")
    private int guestCount;

    @Column(nullable = false)
    private String confirmationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location", nullable = false)
    private Location location;

    @OneToOne(mappedBy = "locationForEvent", optional = false)
    private OrganizedEvent event;

    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_location_for_event")
    private Set<CateringForChosenEventLocation> cateringsForEventLocation = new HashSet<>();


}
