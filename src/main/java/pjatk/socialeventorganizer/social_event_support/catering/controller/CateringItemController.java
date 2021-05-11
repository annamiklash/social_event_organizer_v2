package pjatk.socialeventorganizer.social_event_support.catering.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringItem;
import pjatk.socialeventorganizer.social_event_support.catering.model.request.CateringItemRequest;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/catering_item")
public class CateringItemController {

    private final CateringItemService service;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CateringItem>> findAll() {
        log.info("GET ALL CATERING_ITEM_TYPES");
        return ResponseEntity.ok(service.findAll());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addCatering(@Valid @RequestBody CateringItemRequest request) {

        service.addNewCateringItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCatering(@Valid @RequestBody CateringItemRequest request, @PathVariable Long id) {
        service.updateCateringItem(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}") //name same as function argument
    public ResponseEntity<Void> deleteCatering(@PathVariable Long id) {
        service.deleteCateringItem(id);
        return ResponseEntity.noContent().build();
    }
}
