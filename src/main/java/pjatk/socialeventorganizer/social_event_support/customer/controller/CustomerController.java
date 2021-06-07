package pjatk.socialeventorganizer.social_event_support.customer.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.customer.guest.Guest;
import pjatk.socialeventorganizer.social_event_support.customer.model.request.CreateCustomerAccountRequest;
import pjatk.socialeventorganizer.social_event_support.customer.model.response.CustomerInformationResponse;
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/customer")
public class CustomerController {

    private final CustomerService service;

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "/all",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ImmutableList<Customer>> findAll() {
//        log.info("GET ALL CUSTOMERS");
//        return ResponseEntity.ok(service.findAll());
//    }

    @PreAuthorize("hasAnyAuthority('ADMIN','NEW_USER')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCustomerAccount(@RequestBody @Valid CreateCustomerAccountRequest request) {
        log.info("CREATE CUSTOMER ACCOUNT");
        service.createCustomerAccount(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
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


    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/invitation/sendToAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendScheduleToAllGuests(@RequestParam("orgEventId") long orgEventId) {
        log.info("SEND INVITE TO GUEST");
        service.sendInvitationToGuest(orgEventId);
        return ResponseEntity.badRequest().build();
    }
}


