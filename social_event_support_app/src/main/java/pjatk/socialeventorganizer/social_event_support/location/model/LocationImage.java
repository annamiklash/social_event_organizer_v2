package pjatk.socialeventorganizer.social_event_support.location.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "location_image")
@Entity(name = "location_image")
public class LocationImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location_image")
    Long id;

    String image;

    String alt;

    @Column(name = "id_location")
    Integer locationId;
}
