package pjatk.socialeventorganizer.social_event_support.availability.location.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.availability.Availability;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "location_availability")
@Entity(name = "location_availability")
public class LocationAvailability extends Availability {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Location location;
}
