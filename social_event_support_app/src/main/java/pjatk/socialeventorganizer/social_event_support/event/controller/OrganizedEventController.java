package pjatk.socialeventorganizer.social_event_support.event.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/events")
public class OrganizedEventController {

    private final OrganizedEventService organizedEventService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OrganizedEventDto>> findAll(@RequestParam(required = false) String keyword,
                                                                    @RequestParam(defaultValue = "0") Integer firstResult,
                                                                    @RequestParam(defaultValue = "50") Integer maxResult,
                                                                    @RequestParam(defaultValue = "id") String sort,
                                                                    @RequestParam(defaultValue = "desc") String order) {
        log.info("GET ALL ORG EVENTS");
        return ResponseEntity.ok(organizedEventService.list(new CustomPage(maxResult, firstResult, sort, order), keyword));
    }

    //TODO: test
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizedEventDto> changeEventStatus(@RequestParam long customerId, @RequestParam long eventId,
                                                               @RequestParam EventStatusEnum status) {
        final OrganizedEvent organizedEvent = organizedEventService.changeStatus(customerId, eventId, status);

        return ResponseEntity.ok(OrganizedEventMapper.toDto(organizedEvent));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizedEventDto> create(@RequestBody @Valid OrganizedEventDto dto) {
        final OrganizedEvent organizedEvent = organizedEventService.create(dto);
        return ResponseEntity.ok(OrganizedEventMapper.toDtoWithCustomer(organizedEvent));
    }
}
