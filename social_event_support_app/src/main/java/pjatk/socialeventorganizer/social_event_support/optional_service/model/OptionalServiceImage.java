package pjatk.socialeventorganizer.social_event_support.optional_service.model;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "optional_service_image")
@Entity(name = "optional_service_image")
public class OptionalServiceImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_optional_service_image")
    private Long id;

    private String image;

    private  String alt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private OptionalService service;
}
