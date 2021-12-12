package pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventConfirmationDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.mapper.OptionalServiceForLocationMapper;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.dto.OptionalServiceForChosenLocationDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.service.OptionalServiceForLocationService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/event/location/services")
public class OptionalServiceForLocationController {

    private final OptionalServiceForLocationService optionalServiceForLocationService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "confirm",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionalServiceForChosenLocationDto> confirmReservation(@RequestParam long id,
                                                                                  @RequestParam long eventId) {

        final OptionalServiceForChosenLocation optionalService = optionalServiceForLocationService.confirmReservation(id, eventId);

        return ResponseEntity.ok(OptionalServiceForLocationMapper.toDto(optionalService));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OptionalServiceForChosenLocationDto>> listAllByConfirmationStatus(@RequestParam String status,
                                                                                                          @RequestParam long locationId) {

        List<OptionalServiceForChosenLocation> optionalServices = optionalServiceForLocationService.listAllByStatus(locationId, status);

        return ResponseEntity.ok(
                ImmutableList.copyOf(optionalServices.stream()
                        .map(OptionalServiceForLocationMapper::toDto)
                        .collect(Collectors.toList())));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizedEventConfirmationDto> create(@RequestParam long customerId,
                                                                @RequestParam long eventId,
                                                                @RequestParam long serviceId,
                                                                @RequestBody @Valid OptionalServiceForChosenLocationDto dto) {
        final OptionalServiceForChosenLocation optionalService = optionalServiceForLocationService.create(customerId, eventId, serviceId, dto);

        return ResponseEntity.ok(OrganizedEventMapper.toDtoWithServices(optionalService.getLocationForEvent().getEvent()));
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "cancel",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionalServiceForChosenLocationDto> confirmCancelled(@RequestParam long id) {

        final OptionalServiceForChosenLocation serviceForChosenLocation = optionalServiceForLocationService.setAsCancelled(id);

        return ResponseEntity.ok(OptionalServiceForLocationMapper.toDto(serviceForChosenLocation));
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionalServiceForChosenLocationDto> cancel(@RequestParam long id) {
        final OptionalServiceForChosenLocation serviceForChosenLocation = optionalServiceForLocationService.cancelReservation(id);

        return ResponseEntity.ok(OptionalServiceForLocationMapper.toDto(serviceForChosenLocation));
    }
}
