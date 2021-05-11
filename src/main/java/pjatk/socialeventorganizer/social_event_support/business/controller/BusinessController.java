package pjatk.socialeventorganizer.social_event_support.business.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.Business;
import pjatk.socialeventorganizer.social_event_support.business.model.request.CreateBusinessAccountRequest;
import pjatk.socialeventorganizer.social_event_support.business.service.BusinessService;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/business")
public class BusinessController {

    private final BusinessService service;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<Business>> findAll() {
        log.info("GET ALL BUSINESSES");
        final ImmutableList<Business> all = service.findAll();
        log.info(String.valueOf(all.size()));
        return ResponseEntity.ok(service.findAll());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','NEW_USER')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createBusinessAccount(@Valid @RequestBody CreateBusinessAccountRequest request) {
        service.createBusinessAccount(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
