package pjatk.socialeventorganizer.social_event_support.businesshours.location.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.businesshours.BusinessHours;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "location_business_hours")
@Table(name = "location_business_hours")
public class LocationBusinessHours extends BusinessHours {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Location location;

}
