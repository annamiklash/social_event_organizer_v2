package pjatk.socialeventorganizer.social_event_support.image.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public abstract class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_image")
    private long id;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] image;

    @Column(nullable = false)
    private boolean isMain;

    @Column(nullable = false)
    private String name;

}
