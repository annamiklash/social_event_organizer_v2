package pjatk.socialeventorganizer.social_event_support.model.dto;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "users")
@Table(name = "users")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id_user")
    Long id;

    @Column(name = "hashed_password")
    String password;

    @Column
    String email;

    @Column(name = "user_type")
    Character type;
}
