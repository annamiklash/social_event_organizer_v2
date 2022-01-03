package pjatk.socialeventorganizer.social_event_support.businesshours.location.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.businesshours.BusinessHours;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "location_business_hours")
@Table(name = "location_business_hours")
public class LocationBusinessHours extends BusinessHours {

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Location location;

}
