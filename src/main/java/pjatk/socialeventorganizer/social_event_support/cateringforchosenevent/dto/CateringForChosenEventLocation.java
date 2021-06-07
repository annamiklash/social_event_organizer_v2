package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.Catering;
import pjatk.socialeventorganizer.social_event_support.locationforevent.LocationForEvent;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "id_catering", nullable = false)
    private Catering catering;

    @ManyToOne
    @JoinColumn(name = "id_location_for_event", nullable = false)
    private LocationForEvent eventLocation;

}
