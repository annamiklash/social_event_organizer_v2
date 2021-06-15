package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.dto;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "service_review")
@Entity(name = "service_review")
public class OptionalServiceReview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service_review")
    Long id;

    @Column
    String title;

    @Column
    String comment;

    @Column(name = "id_optional_service")
    Integer optionalServiceId;

    @Column(name = "star_review")
    Integer starRating;

    @Column(name = "id_customer")
    Integer customerId;

}
