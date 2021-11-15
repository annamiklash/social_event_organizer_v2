package pjatk.socialeventorganizer.social_event_support.invite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventInvitationDto {

//    private List<GuestInfoResponse> guestInfo;
//    private OrganizerInfoResponse organizerInfo;
//    private EventInfoResponse eventInfo;
//    private List<LocationInfoResponse> locationInfo;
//    private List<ServicesInfoResponse> serviceInfo;

//    private CustomerDto customer;

    private OrganizedEventDto event;




}
