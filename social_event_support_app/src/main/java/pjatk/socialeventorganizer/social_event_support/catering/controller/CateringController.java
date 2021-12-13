package pjatk.socialeventorganizer.social_event_support.catering.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringMapper;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.FilterCateringsDto;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.reviews.catering.service.CateringReviewService;
import pjatk.socialeventorganizer.social_event_support.table.TableDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/caterings")
public class CateringController {

    private final CateringService cateringService;

    private final CateringReviewService cateringReviewService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<CateringDto>> list(@RequestParam(required = false) String keyword,
                                                           @RequestParam(defaultValue = "0") Integer pageNo,
                                                           @RequestParam(defaultValue = "5") Integer pageSize,
                                                           @RequestParam(defaultValue = "id") String sortBy) {

        log.info("GET ALL CATERING");
        final List<Catering> cateringList = cateringService.list(
                CustomPage.builder()
                        .pageNo(pageNo)
                        .pageSize(pageSize)
                        .sortBy(sortBy).build(), keyword);

        final Long count = cateringService.count(keyword);

        final ImmutableList<CateringDto> result = ImmutableList.copyOf(cateringList.stream()
                .map(CateringMapper::toDto)
                .peek(dto -> dto.setRating(cateringReviewService.getRating(dto.getId())))
                        .collect(Collectors.toList()));

        return ResponseEntity.ok(new TableDto<>(new TableDto.MetaDto(count, pageNo, pageSize, sortBy), result));
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
                        .peek(cateringDto -> cateringDto.setRating(cateringReviewService.getRating(cateringDto.getId())))
                        .collect(Collectors.toList())));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> get(@RequestParam long id) {
        log.info("GET " + id);
        final Catering catering = cateringService.get(id);
        final CateringDto cateringDto = CateringMapper.toDto(catering);
        cateringDto.setRating(cateringReviewService.getRating(id));

        return ResponseEntity.ok(cateringDto);
    }


    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> getWithDetail(@PathVariable long id) {
        log.info("GET " + id);
        final Catering catering = cateringService.getWithDetail(id);

        final CateringDto cateringDto = CateringMapper.toDtoWithDetail(catering);
        cateringDto.setRating(cateringReviewService.getRating(id));

        return ResponseEntity.ok(cateringDto);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "business",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringDto>> getByBusinessId(@RequestParam long id) {
        final ImmutableList<Catering> caterings = cateringService.getByBusinessId(id);

        return ResponseEntity.ok(
                ImmutableList.copyOf(caterings.stream()
                        .map(CateringMapper::toDto)
                        .peek(dto -> dto.setRating(cateringReviewService.getRating(dto.getId())))
                        .collect(Collectors.toList())));
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "location",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringDto>> getByLocationId(@RequestParam long id) {
        final ImmutableList<Catering> caterings = cateringService.getByLocationId(id);

        return ResponseEntity.ok(
                ImmutableList.copyOf(caterings.stream()
                        .map(CateringMapper::toDto)
                        .peek(dto -> dto.setRating(cateringReviewService.getRating(dto.getId())))
                        .collect(Collectors.toList())));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "delete")
    public ResponseEntity<Void> delete(@RequestParam long id) {
        try {
            cateringService.deleteLogical(id);
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

            final CateringDto cateringDto = CateringMapper.toDtoWithDetail(catering);
            cateringDto.setRating(cateringReviewService.getRating(id));

            return ResponseEntity.ok(cateringDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
