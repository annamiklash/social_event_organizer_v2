package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "catering_for_chosen_location")
public class CateringForChosenEventLocation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catering_for_chosen_location")
    private Long id;

    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering", nullable = false)
    private Catering catering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location_for_event", nullable = false)
    private LocationForEvent eventLocation;

    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering_for_chosen_location")
    private Set<CateringOrderChoice> cateringOrder;

}
