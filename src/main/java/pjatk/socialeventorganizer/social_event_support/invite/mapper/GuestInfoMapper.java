package pjatk.socialeventorganizer.social_event_support.invite.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.customer.guest.Guest;
import pjatk.socialeventorganizer.social_event_support.invite.response.GuestInfoResponse;

@Component
public class GuestInfoMapper {

    public GuestInfoResponse mapToResponse(Guest guest) {

        final String firstNameAndLastName = guest.getFirstName() + " " + guest.getLastName();
        return GuestInfoResponse.builder()
                .guestFirstAndLastName(firstNameAndLastName)
                .email(guest.getEmail())
                .build();
    }

}
