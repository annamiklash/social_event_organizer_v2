package pjatk.socialeventorganizer.social_event_support.appproblem.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppProblemResponse {

    private Integer id;

    private String concern;
}