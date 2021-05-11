package pjatk.socialeventorganizer.social_event_support.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.model.dto.Customer;
import pjatk.socialeventorganizer.social_event_support.model.response.CustomerInformationResponse;

@Component
public class CustomerMapper {

    public CustomerInformationResponse mapToCustomerInformationResponse(Customer customer) {
        return CustomerInformationResponse.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .build();
    }
}
