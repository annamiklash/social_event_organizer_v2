package pjatk.socialeventorganizer.social_event_support.invite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.invite.response.EventInfoResponse;
import pjatk.socialeventorganizer.social_event_support.invite.response.GuestInfoResponse;
import pjatk.socialeventorganizer.social_event_support.invite.response.LocationInfoResponse;
import pjatk.socialeventorganizer.social_event_support.invite.response.OrganizerInfoResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InviteContent {

    private List<GuestInfoResponse> guestInfo;
    private OrganizerInfoResponse organizerInfo;
    private EventInfoResponse eventInfo;
    private List<LocationInfoResponse> locationInfo;
//    private List<ServicesInfoResponse> serviceInfo;
}
