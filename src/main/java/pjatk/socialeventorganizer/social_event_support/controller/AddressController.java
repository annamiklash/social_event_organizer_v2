package pjatk.socialeventorganizer.social_event_support.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.service.AddressService;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/address")
public class AddressController {

    private final AddressService service;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/id/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAddressById(@PathVariable Long id) {
        log.info("GET " + id);
        return ResponseEntity.ok(service.findById(id));
    }
}
