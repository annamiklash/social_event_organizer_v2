package pjatk.socialeventorganizer.social_event_support.invite.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.Customer;
import pjatk.socialeventorganizer.social_event_support.invite.response.OrganizerInfoResponse;

@Component
public class OrganizerInfoMapper {

    public OrganizerInfoResponse mapToResponse(Customer customer) {
        final String firstAndLastName = customer.getFirstName() + " " + customer.getLastName();
        return OrganizerInfoResponse.builder()
                .firstAndLastName(firstAndLastName)
                .email(customer.getUser().getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }
}
