package pjatk.socialeventorganizer.social_event_support.location.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.location.mapper.LocationMapper;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.FilterLocationsDto;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/locations")
public class LocationController {

    private final LocationService locationService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationDto>> list(@RequestParam(required = false) String keyword,
                                                           @RequestParam(defaultValue = "0") Integer firstResult,
                                                           @RequestParam(defaultValue = "50") Integer maxResult,
                                                           @RequestParam(defaultValue = "id") String sort,
                                                           @RequestParam(defaultValue = "desc") String order) {
        log.info("GET ALL LOCATIONS");

        final ImmutableList<Location> list = locationService.list(new CustomPage(maxResult, firstResult, sort, order), keyword);

        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(LocationMapper::toDto)
                        .collect(Collectors.toList())));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> get(@RequestParam long id) {
        log.info("GET " + id);

        final Location location = locationService.get(id);

        return ResponseEntity.ok(LocationMapper.toDto(location));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> getWithDetail(@PathVariable long id) {
        log.info("GET " + id);

        final Location location = locationService.getWithDetail(id);

        return ResponseEntity.ok(LocationMapper.toDtoWithDetail(location));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> create(@Valid @RequestBody LocationDto dto) {

        final Location location = locationService.create(dto);

        return ResponseEntity.ok(LocationMapper.toDto(location));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            params = {"id"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> edit(@Valid @RequestBody LocationDto dto, @RequestParam long id) {

        final Location location = locationService.edit(dto, id);

        return ResponseEntity.ok(LocationMapper.toDtoWithDetail(location));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "allowed/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationDto>> searchByAppliedFilters(@Valid @RequestBody FilterLocationsDto dto) {
        log.info("GET LOCATIONS BY DESCRIPTION");

        final ImmutableList<Location> list = locationService.search(dto);

        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(LocationMapper::toDto)
                        .collect(Collectors.toList())));
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/availability",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> getWithAvailability(@PathVariable long id, @RequestParam String date) {
        Location location = locationService.getWithAvailability(id, date);

        return ResponseEntity.ok(LocationMapper.toDtoWithAvailability(location));
    }

    //TODO: edit availability

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            params = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@RequestParam long id) {
        locationService.delete(id);

        return ResponseEntity.noContent().build();
    }


}

