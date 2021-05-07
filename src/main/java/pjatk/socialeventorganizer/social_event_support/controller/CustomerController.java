package pjatk.socialeventorganizer.social_event_support.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.model.dto.Customer;

import pjatk.socialeventorganizer.social_event_support.service.CustomerService;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/customer")
public class CustomerController {

    private final CustomerService service;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<Customer>> findAll() {
        log.info("GET ALL CUSTOMERS");
        return ResponseEntity.ok(service.findAll());
    }
}
