package pjatk.socialeventorganizer.social_event_support.appproblem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.UserDto;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppProblemDto implements Serializable {

    private Long id;

    private String createdAt;

    private String concern;

    private String description;

    private UserDto user;
}
