package pjatk.socialeventorganizer.social_event_support.optional_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TranslationLanguageDto implements Serializable {

    private long id;

    @NotNull
    private String name;
}
