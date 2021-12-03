package pjatk.socialeventorganizer.social_event_support.event.model;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity(name = "organized_event")
public class OrganizedEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_organized_event")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private String eventStatus;

    @Column(nullable = false)
    private int guestCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_event_type", nullable = false)
    private EventType eventType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location_address")
    private LocationForEvent locationForEvent;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_guest",
            joinColumns = @JoinColumn(name = "id_organized_event"),
            inverseJoinColumns = @JoinColumn(name = "id_guest"))
    private Set<Guest> guests;

}
