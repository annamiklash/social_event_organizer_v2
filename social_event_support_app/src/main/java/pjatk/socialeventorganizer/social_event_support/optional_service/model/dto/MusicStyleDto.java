package pjatk.socialeventorganizer.social_event_support.optional_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MusicStyleDto {

    private long id;

    @NotBlank(message = "Name from is mandatory")
    private String name;
}
