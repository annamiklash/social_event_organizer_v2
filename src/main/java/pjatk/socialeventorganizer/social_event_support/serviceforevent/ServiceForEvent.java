package pjatk.socialeventorganizer.social_event_support.serviceforevent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.dto.OptionalService;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "service_for_event")
public class ServiceForEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chosen_service_for_event")
    private Long id;

    @Column(name = "time_from")
    private LocalDateTime dateTimeFrom;

    @Column(name = "time_to")
    private LocalDateTime dateTimeTo;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "id_optional_service", nullable = false)
    private OptionalService optionalService;

    @ManyToOne
    @JoinColumn(name = "id_location_for_event", nullable = false)
    private ServiceForEvent locationforevent;

}
