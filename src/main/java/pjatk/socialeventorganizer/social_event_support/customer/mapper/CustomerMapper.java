package pjatk.socialeventorganizer.social_event_support.customer.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.model.request.CreateCustomerAccountRequest;
import pjatk.socialeventorganizer.social_event_support.customer.model.response.CustomerInformationResponse;
import pjatk.socialeventorganizer.social_event_support.user.model.User;

@Component
public class CustomerMapper {

    public Customer mapToDTO(CreateCustomerAccountRequest request, User user) {
        return Customer.builder()
                .id(user.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(Converter.convertPhoneNumberString(request.getPhoneNumber()))
                .birthdate(Converter.convertDateString(request.getBirthDate()))
                .user(user)
                .build();
    }

    public CustomerInformationResponse mapToCustomerInformationResponse(Customer customer) {
        return CustomerInformationResponse.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getUser().getEmail())
                .address(customer.getAddress())
                .build();
    }
}
