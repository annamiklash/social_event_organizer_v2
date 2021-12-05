package pjatk.socialeventorganizer.social_event_support.location.locationforevent.model;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalTime;
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
    private LocalTime timeFrom;

    @Column(name = "time_to")
    private LocalTime timeTo;

    @Min(1)
    @Column(name = "guests")
    private int guestCount;

    @Column(nullable = false)
    private String confirmationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location", nullable = false)
    private Location location;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_organized_event")
//    @OneToOne(mappedBy = "locationForEvent", optional = false, fetch = FetchType.LAZY)
    private OrganizedEvent event;

    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location_for_event")
    private Set<CateringForChosenEventLocation> cateringsForEventLocation;

    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location_for_event")
    private Set<OptionalServiceForChosenLocation> services;


}
