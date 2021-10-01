package pjatk.socialeventorganizer.social_event_support.appproblem.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppProblemRequest implements Serializable {


    @NotBlank(message = "Concern name is mandatory")
    @Size(min = 1, max = 100, message
            = "The concern should be a sentence between 1 and 100 characters")
    private String concern;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 1, max = 500, message
            = "The name should be between 1 and 500 characters")
    private String description;

}
