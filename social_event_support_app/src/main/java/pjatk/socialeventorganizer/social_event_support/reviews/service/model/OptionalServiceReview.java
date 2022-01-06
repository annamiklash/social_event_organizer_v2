package pjatk.socialeventorganizer.social_event_support.reviews.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.reviews.Review;

import javax.persistence.*;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "service_review")
@Entity(name = "service_review")
public class OptionalServiceReview extends Review {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service", nullable = false)
    private OptionalService optionalService;
}
