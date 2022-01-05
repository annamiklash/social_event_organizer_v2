package pjatk.socialeventorganizer.social_event_support.appproblem.model;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "app_problem")
@Entity(name = "app_problem")
public class AppProblem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_app_problem")
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(nullable = false)
    private String concern;

    @Column(nullable = false)
    private String description;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;
}
