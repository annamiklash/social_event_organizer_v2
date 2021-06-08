package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.dto;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "catering_review")
@Entity(name = "catering_review")
public class CateringReview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catering_review")
    Long id;

    @Column
    String title;

    @Column
    String comment;

    @Column(name = "id_catering")
    Integer cateringId;

    @Column(name = "star_review")
    Integer starRating;

    @Column(name = "id_customer")
    Integer customerId;

}
