package pjatk.socialeventorganizer.social_event_support.appproblem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.UserDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppProblemDto implements Serializable {

    private long id;

    @NotNull
    private String concern;

    @NotNull
    private String description;

    private String createdAt;

    private String resolvedAt;

    private UserDto user;
}
