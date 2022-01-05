package pjatk.socialeventorganizer.social_event_support.user.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.AppProblem;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@Entity(name = "users")
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "hashed_password", nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(name = "user_type", nullable = false)
    private Character type;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "blocked_at")
    private LocalDateTime blockedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private Set<AppProblem> appProblems;
}
