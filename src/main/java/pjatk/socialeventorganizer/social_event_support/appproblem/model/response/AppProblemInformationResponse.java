package pjatk.socialeventorganizer.social_event_support.appproblem.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppProblemInformationResponse {

    private Long id;

    private LocalDateTime dateTime;

    private String concern;

    private String description;

    private Integer userId;
}