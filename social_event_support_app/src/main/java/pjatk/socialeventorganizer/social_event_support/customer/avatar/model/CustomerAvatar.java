package pjatk.socialeventorganizer.social_event_support.customer.avatar.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "customer_avatar")
@Entity(name = "customer_avatar")
public class CustomerAvatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avatar")
    private Long id;

    @Type(type="org.hibernate.type.BinaryType")
    private byte[] image;

}
