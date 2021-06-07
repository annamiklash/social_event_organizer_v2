package pjatk.socialeventorganizer.social_event_support.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.Customer;
import pjatk.socialeventorganizer.social_event_support.locationforevent.LocationForEvent;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
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

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isPredefined;

    private String eventStatus;

    @ManyToOne
    @JoinColumn(name = "id_event_type", nullable = false)
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_organized_event")
    @JsonIgnore
    private Set<LocationForEvent> locationsForEvent = new HashSet<>();



}
