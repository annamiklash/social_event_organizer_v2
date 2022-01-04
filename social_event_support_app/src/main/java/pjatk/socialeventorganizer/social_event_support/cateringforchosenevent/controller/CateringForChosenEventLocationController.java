package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper.CateringForChosenLocationMapper;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service.CateringForChosenEventLocationService;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventConfirmationDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/event/catering")
public class CateringForChosenEventLocationController {

    private final CateringForChosenEventLocationService cateringForChosenEventLocationService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "confirm",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringForChosenEventLocationDto> confirmReservation(@RequestParam long cateringId,
                                                                                @RequestParam long eventId) {

        final CateringForChosenEventLocation catering = cateringForChosenEventLocationService.confirmReservation(cateringId, eventId);

        return ResponseEntity.ok(CateringForChosenLocationMapper.toDto(catering));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringForChosenEventLocationDto>> listAllByConfirmationStatus(@RequestParam String status,
                                                                                                        @RequestParam long cateringId) {

        List<CateringForChosenEventLocation> caterings = cateringForChosenEventLocationService.listAllByStatus(cateringId, status);

        return ResponseEntity.ok(
                ImmutableList.copyOf(caterings.stream()
                        .map(CateringForChosenLocationMapper::toDto)
                        .collect(Collectors.toList())));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizedEventConfirmationDto> create(@RequestParam long customerId,
                                                                @RequestParam long eventId,
                                                                @RequestParam long cateringId,
                                                                @RequestBody @Valid CateringForChosenEventLocationDto dto) {
        final CateringForChosenEventLocation catering = cateringForChosenEventLocationService.create(customerId, eventId, cateringId, dto);

        return ResponseEntity.ok(OrganizedEventMapper.toDtoWithCatering(catering.getEventLocation().getEvent()));
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringForChosenEventLocationDto> cancel(@RequestParam long id) {
        final CateringForChosenEventLocation locationForEvent = cateringForChosenEventLocationService.cancelReservation(id);

        return ResponseEntity.ok(CateringForChosenLocationMapper.toDto(locationForEvent));
    }
}

