package pjatk.socialeventorganizer.social_event_support.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.common.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.common.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.model.dto.Customer;
import pjatk.socialeventorganizer.social_event_support.model.dto.Guest;
import pjatk.socialeventorganizer.social_event_support.model.exception.InvalidCredentialsException;
import pjatk.socialeventorganizer.social_event_support.model.exception.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.model.request.LoginRequest;
import pjatk.socialeventorganizer.social_event_support.model.response.CustomerInformationResponse;
import pjatk.socialeventorganizer.social_event_support.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    SecurityService securityService;

    public ImmutableList<Customer> findAll() {
        final List<Customer> customerList = customerRepository.findAll();
        return ImmutableList.copyOf(customerList);
    }

    public CustomerInformationResponse getCustomerInformation() {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        final Optional<Customer> optionalCustomer = customerRepository.findById(userCredentials.getUserId());
        if (optionalCustomer.isPresent()) {
            final Customer customer = optionalCustomer.get();
            return customerMapper.mapToCustomerInformationResponse(customer);
        }
        throw new NotFoundException("Cannot find customer with id " + userCredentials.getCustomerId());

    }


    public Optional<Set<Guest>> findAllCustomerGuests() {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        final Optional<Customer> optionalCustomer = customerRepository.findById(userCredentials.getUserId());
        if (optionalCustomer.isPresent()) {
            final Customer customer = optionalCustomer.get();
            return Optional.of(customer.getGuests());
        }
        throw new NotFoundException("Cannot find customer with id " + userCredentials.getCustomerId());

    }

    public Customer getCustomerByEmail(String email) {
        final Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();
        }
        throw new InvalidCredentialsException("Please check log in credentials");

    }

    public Customer getCustomerByEmailAndPassword(LoginRequest request) {
        final Optional<Customer> optionalCustomer = customerRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();
        }
        throw new InvalidCredentialsException("Please check log in credentials");

    }


}
