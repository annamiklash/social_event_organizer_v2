package pjatk.socialeventorganizer.social_event_support.invite.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringPlaceInfoResponse {

    private String cateringName;
    private LocalDateTime dateTime;
}
