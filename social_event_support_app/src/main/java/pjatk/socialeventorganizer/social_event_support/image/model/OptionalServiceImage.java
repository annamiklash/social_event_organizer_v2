package pjatk.socialeventorganizer.social_event_support.image.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "optional_service_image")
@Entity(name = "optional_service_image")
public class OptionalServiceImage extends Image{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_image")
    private OptionalService service;
}
