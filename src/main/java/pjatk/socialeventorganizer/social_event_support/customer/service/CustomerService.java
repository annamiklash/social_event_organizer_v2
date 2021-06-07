package pjatk.socialeventorganizer.social_event_support.customer.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.request.AddressRequest;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.customer.guest.Guest;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.model.request.CreateCustomerAccountRequest;
import pjatk.socialeventorganizer.social_event_support.customer.model.response.CustomerInformationResponse;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.ForbiddenAccessException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

    CustomerRepository customerRepository;

    CustomerMapper customerMapper;

    AddressMapper addressMapper;

    SecurityService securityService;

    UserService userService;

    AddressService addressService;

    public ImmutableList<Customer> findAll() {
        final List<Customer> customerList = customerRepository.findAll();
        return ImmutableList.copyOf(customerList);
    }

    @Transactional
    public void createCustomerAccount(CreateCustomerAccountRequest customerRequest) {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        if (!userCredentials.getIsNewAccount()) {
            throw new ForbiddenAccessException("Cannot access");
        }
        final AddressRequest addressRequest = customerRequest.getAddressRequest();
        final Address address = addressMapper.mapToDTO(addressRequest);
        addressService.save(address);

        final User userById = userService.getUserById(userCredentials.getUserId());

        final Customer customer = customerMapper.mapToDTO(customerRequest, userById);
        customer.setAddress(address);

        log.info("TRYING TO SAVE CUSTOMER");
        customerRepository.save(customer);

        userCredentials.setIsNewAccount(false);
    }

    public CustomerInformationResponse getCustomerInformation() {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        final Optional<Customer> optionalCustomer = customerRepository.findByUser_Id(userCredentials.getUserId());
        if (optionalCustomer.isPresent()) {
            final Customer customer = optionalCustomer.get();
            return customerMapper.mapToCustomerInformationResponse(customer);
        }
        throw new NotFoundException("Cannot find customer with id " + userCredentials.getUserId());

    }

    public Optional<Set<Guest>> findAllCustomerGuests() {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        final Optional<Customer> optionalCustomer = customerRepository.findByUser_Id(userCredentials.getUserId());
        if (optionalCustomer.isPresent()) {
            final Customer customer = optionalCustomer.get();
            return Optional.of(customer.getGuests());
        }
        throw new NotFoundException("Cannot find customer with id " + userCredentials.getUserId());

    }
}
