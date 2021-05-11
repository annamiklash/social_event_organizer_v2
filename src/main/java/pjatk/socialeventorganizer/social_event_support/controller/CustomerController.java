package pjatk.socialeventorganizer.social_event_support.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.model.dto.Customer;

import pjatk.socialeventorganizer.social_event_support.model.dto.Guest;
import pjatk.socialeventorganizer.social_event_support.model.exception.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.model.response.CustomerInformationResponse;
import pjatk.socialeventorganizer.social_event_support.service.CustomerService;

import java.util.Optional;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/customer")
public class CustomerController {

    private final CustomerService service;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<Customer>> findAll() {
        log.info("GET ALL CUSTOMERS");
        return ResponseEntity.ok(service.findAll());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/one",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerInformationResponse> findCustomerInformation() {
        log.info("GET CUSTOMER");
        try {
            return ResponseEntity.ok(service.getCustomerInformation());
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();

        }
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/guests",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Optional<Set<Guest>>> findAllCustomerGuests() {
        log.info("GET CUSTOMER GUESTS");
        try {
            return ResponseEntity.ok(service.findAllCustomerGuests());
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();

        }
    }
}
