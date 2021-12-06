package pjatk.socialeventorganizer.social_event_support.optional_service.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.optional_service.enums.KidPerformerTypeEnum;
import pjatk.socialeventorganizer.social_event_support.optional_service.enums.MusicStyleEnum;
import pjatk.socialeventorganizer.social_event_support.optional_service.enums.OptionalServiceTypeEnum;
import pjatk.socialeventorganizer.social_event_support.optional_service.mapper.OptionalServiceMapper;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.FilterOptionalServiceDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/services")
public class OptionalServiceController {

    private final OptionalServiceService optionalServiceService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OptionalServiceDto>> list(@RequestParam(required = false) String keyword,
                                                                  @RequestParam(defaultValue = "0") Integer firstResult,
                                                                  @RequestParam(defaultValue = "50") Integer maxResult,
                                                                  @RequestParam(defaultValue = "id") String sort,
                                                                  @RequestParam(defaultValue = "desc") String order) {
        log.info("GET ALL SERVICES");

        final ImmutableList<OptionalService> list = optionalServiceService.list(new CustomPage(maxResult, firstResult, sort, order), keyword);

        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(OptionalServiceMapper::toDto)
                        .collect(Collectors.toList())));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionalServiceDto> get(@RequestParam long id) {
        log.info("GET " + id);

        final OptionalService optionalService = optionalServiceService.get(id);

        return ResponseEntity.ok(OptionalServiceMapper.toDto(optionalService));
    }

    //TODO: add method getWithDetail


    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionalServiceDto> create(@Valid @RequestBody OptionalServiceDto dto) {

        final OptionalService optionalService = optionalServiceService.create(dto);

        return ResponseEntity.ok(OptionalServiceMapper.toDto(optionalService));
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            params = {"id"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionalServiceDto> edit(@Valid @RequestBody OptionalServiceDto dto, @RequestParam long id) {

        final OptionalService optionalService = optionalServiceService.edit(dto, id);

        return ResponseEntity.ok(OptionalServiceMapper.toDto(optionalService));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            params = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@RequestParam long id) {
        optionalServiceService.deleteLogical(id);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "allowed/types",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OptionalServiceTypeEnum>> types() {

        return ResponseEntity.ok(ImmutableList.copyOf(List.of(OptionalServiceTypeEnum.values())));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "allowed/kid/performer/types",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<KidPerformerTypeEnum>> kidPerformerTypes() {

        return ResponseEntity.ok(ImmutableList.copyOf(List.of(KidPerformerTypeEnum.values())));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "allowed/music/styles",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<MusicStyleEnum>> musicStyles() {

        return ResponseEntity.ok(ImmutableList.copyOf(List.of(MusicStyleEnum.values())));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "allowed/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OptionalServiceDto>> searchByAppliedFilters(@RequestBody FilterOptionalServiceDto dto) {

        final ImmutableList<OptionalService> list = optionalServiceService.search(dto);

        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(OptionalServiceMapper::toDto)
                        .collect(Collectors.toList())));
    }

}
