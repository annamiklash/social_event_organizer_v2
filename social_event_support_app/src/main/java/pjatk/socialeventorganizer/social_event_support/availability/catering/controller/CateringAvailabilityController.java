package pjatk.socialeventorganizer.social_event_support.availability.catering.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.availability.catering.model.CateringAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.catering.service.CateringAvailabilityService;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/availability/catering")
public class CateringAvailabilityController {

    private final CateringAvailabilityService cateringAvailabilityService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AvailabilityDto>> list(@RequestParam @NotNull long id,
                                                               @RequestParam @NotNull String date) {

        final List<CateringAvailability> availabilities = cateringAvailabilityService.findAllByCateringIdAndDate(id, date);

        return ResponseEntity.ok(
                ImmutableList.copyOf(availabilities.stream()
                        .map(AvailabilityMapper::toDto)
                        .collect(Collectors.toList())));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AvailabilityDto>> create(@Valid @RequestBody AvailabilityDto[] dtos,
                                                                 @RequestParam @NotNull long id) {
        final List<CateringAvailability> availabilities = cateringAvailabilityService.create(Arrays.asList(dtos), id);

        return ResponseEntity.ok(
                ImmutableList.copyOf(availabilities.stream()
                        .map(AvailabilityMapper::toDto)
                        .collect(Collectors.toList())));
    }

    //TODO: edit

    //TODO: delete
}
