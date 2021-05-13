package pjatk.socialeventorganizer.social_event_support.appproblem;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
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

    @Column(name = "id_user")
    Integer userId;
}
