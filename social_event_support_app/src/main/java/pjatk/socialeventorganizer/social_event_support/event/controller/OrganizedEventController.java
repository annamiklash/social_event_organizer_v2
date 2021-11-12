package pjatk.socialeventorganizer.social_event_support.event.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService;

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
}
