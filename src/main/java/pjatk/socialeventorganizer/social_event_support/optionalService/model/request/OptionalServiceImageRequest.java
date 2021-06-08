package pjatk.socialeventorganizer.social_event_support.optionalService.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.image.model.request.ImageRequestDetails;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionalServiceImageRequest {

    @NotNull
    List<ImageRequestDetails> details;

    @NotNull
    Integer optionalServiceId;
}
