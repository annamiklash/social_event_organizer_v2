package pjatk.socialeventorganizer.social_event_support.image.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public  class Image {

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
