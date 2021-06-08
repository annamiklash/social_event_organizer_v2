package pjatk.socialeventorganizer.social_event_support.optionalService.model.dto;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "optional_service_image")
@Entity(name = "optional_service_image")
public class OptionalServiceImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_optional_service_image")
    Long id;

    String image;

    String alt;

    @Column(name = "id_optional_service")
    Integer optionalServiceId;
}
