package pjatk.socialeventorganizer.social_event_support.catering.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.catering.model.request.CateringRequest;
import pjatk.socialeventorganizer.social_event_support.catering.model.response.CateringInformationResponse;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/catering")
public class CateringController {

    private final CateringService cateringService;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringInformationResponse>> findAll() {
        log.info("GET ALL CATERING");
        return ResponseEntity.ok(cateringService.findAll());
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/city/{city}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringInformationResponse>> findByCity(@PathVariable String city) {
        log.info("GET " + city);
        return ResponseEntity.ok(cateringService.findByCity(city));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/name/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringInformationResponse>> findByCateringName(@PathVariable String name) {
        log.info("GET " + name);
        return ResponseEntity.ok(cateringService.findByName(name));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addCatering(@Valid @RequestBody CateringRequest request) {
        cateringService.addNewCatering(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/{cateringId}/?address={addressId}", //name same as function argument
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCatering(@Valid @RequestBody CateringRequest request, @PathVariable Long cateringId, @PathVariable Long addressId) {
        try {
            cateringService.updateCatering(cateringId, request, addressId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}") //name same as function argument
    public ResponseEntity<Void> deleteCatering(@PathVariable Long id) {
        try {
            cateringService.deleteCatering(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
}
