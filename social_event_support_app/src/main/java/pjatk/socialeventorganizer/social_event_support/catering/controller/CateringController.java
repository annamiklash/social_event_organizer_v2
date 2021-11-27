package pjatk.socialeventorganizer.social_event_support.catering.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.address.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringMapper;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.FilterCateringsDto;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/caterings")
public class CateringController {

    private final CateringService cateringService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringDto>> list(@RequestParam(defaultValue = "0") Integer firstResult,
                                                           @RequestParam(defaultValue = "50") Integer maxResult,
                                                           @RequestParam(defaultValue = "id") String sort,
                                                           @RequestParam(defaultValue = "desc") String order,
                                                           @RequestParam(required = false) String keyword) {
        log.info("GET ALL CATERING");
        final List<Catering> cateringList = cateringService.list(new CustomPage(maxResult, firstResult, sort, order), keyword);
        return ResponseEntity.ok(
                ImmutableList.copyOf(cateringList.stream()
                        .map(CateringMapper::toDto)
                        .collect(Collectors.toList())));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "allowed/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringDto>> searchByAppliedFilters(@RequestBody FilterCateringsDto dto) {

        final ImmutableList<Catering> list = cateringService.search(dto);

        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(CateringMapper::toDto)
                        .collect(Collectors.toList())));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> get(@RequestParam long id) {
        log.info("GET " + id);
        final Catering catering = cateringService.get(id);

        return ResponseEntity.ok(CateringMapper.toDto(catering));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> getWithDetail(@PathVariable long id) {
        log.info("GET " + id);
        final Catering catering = cateringService.getWithDetail(id);

        return ResponseEntity.ok(CateringMapper.toDtoWithDetail(catering));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "delete")
    public ResponseEntity<Void> delete(@RequestParam long id) {
        try {
            cateringService.deleteCatering(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "new/location",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> create(@Valid @RequestBody CateringDto dto, @RequestParam long locationId) {
        final Catering catering = cateringService.create(dto, locationId);

        return ResponseEntity.ok(CateringMapper.toDto(catering));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> createStandalone(@Valid @RequestBody CateringDto dto) {
        final Catering catering = cateringService.create(dto, null);

        return ResponseEntity.ok(CateringMapper.toDto(catering));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "edit",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> edit(@Valid @RequestBody CateringDto dto, @RequestParam long id) {
        try {
            final Catering catering = cateringService.edit(id, dto);

            return ResponseEntity.ok(CateringMapper.toDto(catering));
        } catch (IllegalArgumentException e) {

            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{id}/availability",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addAvailability(@Valid @RequestBody AvailabilityDto[] dtos, @PathVariable long id) {
        cateringService.addAvailability(Arrays.asList(dtos), id);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/availability",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> getWithAvailability(@PathVariable long id, @RequestParam String date) {
        Catering catering = cateringService.getWithAvailability(id, date);

        return ResponseEntity.ok(CateringMapper.toDtoWithAvailability(catering));
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/{id}/address",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> editAddress(@Valid @RequestBody AddressDto dto, @PathVariable long id) {
        try {
            final Catering catering = cateringService.editAddress(id, dto);
            return ResponseEntity.ok(CateringMapper.toDto(catering));
        } catch (IllegalArgumentException e) {

            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/address",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressDto> getCateringAddress(@PathVariable long id) {
        try {
            final Address address = cateringService.getAddress(id);

            return ResponseEntity.ok(AddressMapper.toDto(address));
        } catch (IllegalArgumentException e) {

            return ResponseEntity.notFound().build();
        }
    }

}
