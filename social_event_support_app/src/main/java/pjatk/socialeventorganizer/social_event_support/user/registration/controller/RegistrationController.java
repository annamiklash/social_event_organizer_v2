package pjatk.socialeventorganizer.social_event_support.user.registration.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.business.mapper.BusinessMapper;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto;
import pjatk.socialeventorganizer.social_event_support.business.service.BusinessService;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.user.mapper.UserMapper;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.BusinessUserRegistrationDto;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.CustomerUserRegistrationDto;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.UserDto;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api")
public class RegistrationController {

    private final UserService userService;

    private final CustomerService customerService;

    private final BusinessService businessService;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "register/business",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessDto> businessSignup(@RequestBody @Valid BusinessUserRegistrationDto dto) {
        final UserDto user = UserMapper.toDto(userService.registerUser(UserDto.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .type(dto.getType())
                .build()));
        dto.setUser(user);

        final Business business = businessService.createBusinessAccount(BusinessMapper.fromBusinessUserRegistrationDto(dto));
        return ResponseEntity.ok(BusinessMapper.toDtoWithUser(business));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "register/customer",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> customerSignup(@RequestBody @Valid CustomerUserRegistrationDto dto) {
        final UserDto user = UserMapper.toDto(userService.registerUser(UserDto.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .type(dto.getType())
                .build()));
        dto.setUser(user);

        final Customer customer = customerService.create(CustomerMapper.fromCustomerRegistrationDto(dto));
        return ResponseEntity.ok(CustomerMapper.toDtoWithUser(customer));
    }
}
