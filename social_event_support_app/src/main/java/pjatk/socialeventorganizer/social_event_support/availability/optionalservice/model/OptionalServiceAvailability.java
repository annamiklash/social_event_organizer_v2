package pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.availability.Availability;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "optional_service_availability")
@Entity(name = "optional_service_availability")
public class OptionalServiceAvailability extends Availability {

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private OptionalService optionalService;
}
