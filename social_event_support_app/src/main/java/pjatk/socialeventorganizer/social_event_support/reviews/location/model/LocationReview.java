package pjatk.socialeventorganizer.social_event_support.reviews.location.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.reviews.Review;

import javax.persistence.*;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "location_review")
@Entity(name = "location_review")
public class LocationReview extends Review {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location", nullable = false)
    private Location location;

}
