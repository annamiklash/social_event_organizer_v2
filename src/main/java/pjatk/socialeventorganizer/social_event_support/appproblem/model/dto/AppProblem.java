package pjatk.socialeventorganizer.social_event_support.appproblem.model.dto;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pjatk.socialeventorganizer.social_event_support.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "app_problem")
@Entity(name = "app_problem")
public class AppProblem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_app_problem")
    Long id;

    @Column(name = "date_time")
    LocalDateTime dateTime;

    @Column
    String concern;

    @Column
    String description;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_user")
//    @Fetch(FetchMode.JOIN)
//    private User userId;

    @Column(name = "id_user")
    Integer userId;
}
