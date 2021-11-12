package pjatk.socialeventorganizer.social_event_support.image.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {

    List<Long> newImageIds = new ArrayList<>();
}
