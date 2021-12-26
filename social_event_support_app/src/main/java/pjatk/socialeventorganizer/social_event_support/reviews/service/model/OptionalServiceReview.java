package pjatk.socialeventorganizer.social_event_support.reviews.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "service_review")
@Entity(name = "service_review")
public class OptionalServiceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service_review")
    private Long id;

    @Column
    private String title;

    @Column
    private String comment;

    @Column(name = "star_rating")
    private int starRating;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service", nullable = false)
    private OptionalService optionalService;
}
