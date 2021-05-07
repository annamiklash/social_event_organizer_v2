package pjatk.socialeventorganizer.social_event_support.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringResponse {

    private Long id;
    private String name;
    private String email;
}
