package pjatk.socialeventorganizer.social_event_support.invite.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuestInfoResponse {

    private String guestFirstAndLastName;
    private String email;
}