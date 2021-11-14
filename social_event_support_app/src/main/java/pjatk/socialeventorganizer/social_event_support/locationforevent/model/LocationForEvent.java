package pjatk.socialeventorganizer.social_event_support.locationforevent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.dto.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest;
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

    @ManyToOne
    @JoinColumn(name = "id_organized_event", nullable = false)
    private OrganizedEvent event;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_location_dor_event")
    private Set<CateringForChosenEventLocation> cateringsForEventLocation = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "location_for_event_guest",
            joinColumns = @JoinColumn(name = "id_location_for_event"),
            inverseJoinColumns = @JoinColumn(name = "id_guest"))
    private Set<Guest> guests = new HashSet<>();
}
