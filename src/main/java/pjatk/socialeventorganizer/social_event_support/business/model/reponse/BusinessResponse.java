package pjatk.socialeventorganizer.social_event_support.business.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessResponse {

    private Long id;

    private String email;
}
