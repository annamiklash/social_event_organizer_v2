package pjatk.socialeventorganizer.social_event_support.availability.optionalservice.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.service.OptionalServiceAvailabilityService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/availability/service")
public class OptionalServiceAvailabilityController {

    private final OptionalServiceAvailabilityService optionalServiceAvailabilityService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AvailabilityDto>> list(@RequestParam @NotNull long id,
                                                               @RequestParam @NotNull String date) {

        final List<OptionalServiceAvailability> availabilities =
                optionalServiceAvailabilityService.findAllByServiceIdAndDate(id, date);
        final ImmutableList<AvailabilityDto> resultList = availabilities.stream()
                .map(AvailabilityMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @RequestMapping(
            path = "allowed/period",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AvailabilityDto>> listForPeriod(@RequestParam long id,
                                                                     @RequestParam String dateFrom, @RequestParam String dateTo) {

        final List<OptionalServiceAvailability> availabilities = optionalServiceAvailabilityService.findAllByLocationIdAndDatePeriod(id, dateFrom, dateTo);
        final ImmutableList<AvailabilityDto> resultList = availabilities.stream()
                .map(AvailabilityMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AvailabilityDto>> create(@Valid @RequestBody AvailabilityDto[] dtos,
                                                                 @RequestParam @NotNull long id) {
        final ImmutableList<AvailabilityDto> availabilityDtoList = ImmutableList.copyOf(dtos);
        final List<OptionalServiceAvailability> availabilities =
                optionalServiceAvailabilityService.update(availabilityDtoList, id, true);

        final ImmutableList<AvailabilityDto> resultList = availabilities.stream()
                .map(AvailabilityMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AvailabilityDto>> delete(@RequestParam long id,
            @RequestParam String date){
        optionalServiceAvailabilityService.delete(id, date);

        return ResponseEntity.ok().build();
    }
}
