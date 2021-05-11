package pjatk.socialeventorganizer.social_event_support.catering.model.dto;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "catering_image")
@Entity(name = "catering_image")
public class CateringImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catering_image")
    Long id;

    @Column
    String image;

    @Column
    String alt;

    @Column(name = "id_catering")
    Integer cateringId;
}