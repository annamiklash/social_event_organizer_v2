package pjatk.socialeventorganizer.social_event_support.invite.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.invite.response.OrganizerInfoResponse;

@UtilityClass
public class OrganizerInfoMapper {

    public OrganizerInfoResponse mapToDto(Customer customer) {
        final String firstAndLastName = customer.getFirstName() + " " + customer.getLastName();
        return OrganizerInfoResponse.builder()
                .firstAndLastName(firstAndLastName)
                .email(customer.getUser().getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }
}
