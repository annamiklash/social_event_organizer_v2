package pjatk.socialeventorganizer.social_event_support.optionalService.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.request.OptionalServiceRequest;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.response.OptionalServiceInformationResponse;
import pjatk.socialeventorganizer.social_event_support.optionalService.service.OptionalServiceService;


import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/optional_service")
public class OptionalServiceController {

    private final OptionalServiceService optionalServiceService;


    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OptionalServiceInformationResponse>> findAAll() {
        log.info("GET ALL SERVICE");
        return ResponseEntity.ok(optionalServiceService.findAll());
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/alias/{alias}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OptionalServiceInformationResponse>> findByOptionalServiceAlias(@PathVariable String alias) {
        log.info("GET " + alias);
        return ResponseEntity.ok(optionalServiceService.findByAlias(alias));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/id/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findByOptionalServiceId(@PathVariable Long id) {
        log.info("GET " + id);
        return ResponseEntity.ok(optionalServiceService.findById(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addOptionalService(@Valid @RequestBody OptionalServiceRequest request) {
        optionalServiceService.addNewOptionalService(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
