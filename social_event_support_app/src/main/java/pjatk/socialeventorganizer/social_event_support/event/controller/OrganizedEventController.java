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
import pjatk.socialeventorganizer.social_event_support.enums.CustomerReservationTabEnum;
import pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
                                                                    @RequestParam(defaultValue = "0") Integer pageNo,
                                                                    @RequestParam(defaultValue = "50") Integer pageSize,
                                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                                    @RequestParam(defaultValue = "asc") String order)  {
        log.info("GET ALL ORG EVENTS");
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build();
        final ImmutableList<OrganizedEventDto> result = organizedEventService.list(customPage, keyword);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            path = "detail",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizedEventDto> get(@RequestParam long eventId, @RequestParam long customerId) {
        final OrganizedEvent organizedEvent = organizedEventService.getWithDetail(eventId, customerId);

        return ResponseEntity.ok(OrganizedEventMapper.toDtoWithDetail(organizedEvent));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            path = "customer",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OrganizedEventDto>> findAllByCustomerIdAndTab(@RequestParam long customerId,
                                                                                      @RequestParam CustomerReservationTabEnum tab) {
        final List<OrganizedEvent> list = organizedEventService.getAllByCustomerIdAndTab(customerId, tab);

        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(OrganizedEventMapper::toDtoWithCustomerAndEventType)
                        .collect(Collectors.toList())));
    }

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
    public ResponseEntity<OrganizedEventDto> create(@RequestParam long customerId,
                                                    @RequestBody @Valid OrganizedEventDto dto) {
        final OrganizedEvent organizedEvent = organizedEventService.create(customerId, dto);
        return ResponseEntity.ok(OrganizedEventMapper.toDtoWithCustomer(organizedEvent));
    }

}
