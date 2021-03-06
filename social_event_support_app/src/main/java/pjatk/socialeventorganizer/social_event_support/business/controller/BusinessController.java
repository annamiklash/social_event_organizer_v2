package pjatk.socialeventorganizer.social_event_support.business.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.business.mapper.BusinessMapper;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto;
import pjatk.socialeventorganizer.social_event_support.business.service.BusinessService;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/business")
public class BusinessController {

    private final BusinessService businessService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<BusinessDto>> list(@RequestParam(defaultValue = "0") Integer firstResult,
                                                           @RequestParam(defaultValue = "50") Integer maxResult,
                                                           @RequestParam(defaultValue = "id") String sort,
                                                           @RequestParam(defaultValue = "asc") String order) {
        final ImmutableList<Business> list = businessService.list(new CustomPage(maxResult, firstResult, sort, order));

        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(BusinessMapper::toDto)
                        .collect(Collectors.toList())));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            params = {"id"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessDto> get(@RequestParam long id) {
        log.info("GET " + id);
        final Business business = businessService.get(id);

        return ResponseEntity.ok(BusinessMapper.toDto(business));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessDto> detail(@PathVariable long id) {
        log.info("GET " + id);
        final Business business = businessService.getWithDetail(id);
        return ResponseEntity.ok(BusinessMapper.toDtoWithDetail(business));
    }


    @PreAuthorize("hasAnyAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessDto> create(@Valid @RequestBody BusinessDto dto) {

        final Business business = businessService.createBusinessAccount(dto);

        return ResponseEntity.ok(BusinessMapper.toDto(business));
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessDto> edit(@RequestParam long id, @Valid @RequestBody BusinessDto dto) {

        final Business business = businessService.edit(id, dto);

        return ResponseEntity.ok(BusinessMapper.toDto(business));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@RequestParam long id) {
        businessService.deleteLogical(id);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "verify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessDto> verify(@RequestParam long id) {
        final Business business = businessService.verify(id);

        return ResponseEntity.ok(BusinessMapper.toDto(business));
    }

}
