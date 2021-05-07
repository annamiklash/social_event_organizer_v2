package pjatk.socialeventorganizer.social_event_support.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationImageRequest {

    @NotNull
    List<ImageRequestDetails> details;

    @NotNull
    Integer locationId;
}
