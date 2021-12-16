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
import pjatk.socialeventorganizer.social_event_support.table.TableDto;

import javax.validation.Valid;
import java.util.List;
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
    public ResponseEntity<TableDto<LocationDto>> list(@RequestParam(required = false) String keyword,
                                                      @RequestParam(defaultValue = "0") Integer pageNo,
                                                      @RequestParam(defaultValue = "5") Integer pageSize,
                                                      @RequestParam(defaultValue = "id") String sortBy) {
        log.info("GET ALL LOCATIONS");
        final List<Location> list = locationService.list(
                CustomPage.builder()
                        .pageNo(pageNo)
                        .pageSize(pageSize)
                        .sortBy(sortBy).build(), keyword);
        final Long count = locationService.count(keyword);

        final ImmutableList<LocationDto> result = ImmutableList.copyOf(list.stream()
                .map(LocationMapper::toDto)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(new TableDto<>(new TableDto.MetaDto(count, pageNo, pageSize, sortBy), result));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> get(@RequestParam long id) {
        log.info("GET " + id);

        final Location location = locationService.getWithMainImage(id);
        final LocationDto dto = LocationMapper.toDto(location);

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/cities",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getCities() {
        final List<String> result = locationService.getCities();

        return ResponseEntity.ok(result);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> getWithDetail(@PathVariable long id) {
        log.info("GET " + id);

        final Location location = locationService.getWithDetail(id);
        final LocationDto dto = LocationMapper.toDtoWithDetail(location);

        return ResponseEntity.ok(dto);
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
        final LocationDto locationDto = LocationMapper.toDto(location);

        return ResponseEntity.ok(locationDto);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "allowed/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationDto>> searchByAppliedFilters(
            @Valid @RequestBody FilterLocationsDto dto) {

        final ImmutableList<Location> list = locationService.search(dto);

        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(LocationMapper::toDto)
//                        .peek(locationDto -> locationDto.setRating(locationReviewService.getRating(locationDto.getId())))
                        .collect(Collectors.toList())));
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'BUSINESS', 'ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/availability",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> getWithAvailability(@PathVariable long id, @RequestParam String date) {
        final Location location = locationService.getWithAvailability(id, date);
        final LocationDto locationDto = LocationMapper.toDto(location);

        final LocationDto locationDto = LocationMapper.toDto(location);

        return ResponseEntity.ok(locationDto);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "business",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationDto>> getByBusinessId(@RequestParam long id) {
        final ImmutableList<Location> locations = locationService.getByBusinessId(id);

        return ResponseEntity.ok(
                ImmutableList.copyOf(locations.stream()
                        .map(LocationMapper::toDto)
                        .collect(Collectors.toList())));
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "catering",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationDto>> getByCateringId(@RequestParam long cateringId) {
        final ImmutableList<Location> locations = locationService.getByCateringId(cateringId);

        return ResponseEntity.ok(
                ImmutableList.copyOf(locations.stream()
                        .map(LocationMapper::toDto)
                        .collect(Collectors.toList())));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam long id) {
        locationService.deleteLogical(id);

        return ResponseEntity.noContent().build();
    }

}

