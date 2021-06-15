package pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.dto;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "location_review")
@Entity(name = "location_review")
public class LocationReview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location_review")
    Long id;

    @Column
    String title;

    @Column
    String comment;

    @Column(name = "id_location")
    Integer locationId;

    @Column(name = "star_review")
    Integer starRating;

    @Column(name = "id_customer")
    Integer customerId;

}
