package pjatk.socialeventorganizer.social_event_support.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.model.dto.Customer;
import pjatk.socialeventorganizer.social_event_support.model.exception.InvalidCredentialsException;
import pjatk.socialeventorganizer.social_event_support.model.request.LoginRequest;
import pjatk.socialeventorganizer.social_event_support.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

    CustomerRepository customerRepository;

    public ImmutableList<Customer> findAll() {
        final List<Customer> customerList = customerRepository.findAll();
        return ImmutableList.copyOf(customerList);
    }

    public Customer getCustomerByEmailAndPassword(LoginRequest request) {
        final Optional<Customer> optionalCustomer = customerRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();
        }
        throw new InvalidCredentialsException("Please check log in credentials");

    }
}
