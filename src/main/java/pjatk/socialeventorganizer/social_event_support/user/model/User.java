package pjatk.socialeventorganizer.social_event_support.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblem;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity(name = "users")
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    Long id;

    @Column(name = "hashed_password")
    String password;

    @Column
    String email;

    @Column(name = "user_type")
    Character type;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JsonIgnore
    Set<AppProblem> appProblems = new HashSet<>();
}
