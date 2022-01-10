package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper.CateringOrderChoiceMapper;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringOrderChoice;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringOrderChoiceDto;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service.CateringOrderChoiceService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/catering/order")
public class CateringOrderChoiceController {

    private final CateringOrderChoiceService cateringOrderChoiceService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringOrderChoiceDto>> getAll(@RequestParam long cateringId) {

        final ImmutableList<CateringOrderChoice> cateringOrderChoiceList = cateringOrderChoiceService.getAll(cateringId);
        final ImmutableList<CateringOrderChoiceDto> resultList = cateringOrderChoiceList.stream()
                .map(CateringOrderChoiceMapper::toDtoWithItem)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER', 'BUSINESS')")
    @RequestMapping(
            path = "reservation",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringOrderChoiceDto>> getAll(@RequestParam long cateringId,
                                                                        @RequestParam long reservationId) {

        final ImmutableList<CateringOrderChoice> cateringOrderChoiceList = cateringOrderChoiceService.getAll(cateringId, reservationId);
        final ImmutableList<CateringOrderChoiceDto> resultList = cateringOrderChoiceList.stream()
                .map(CateringOrderChoiceMapper::toDtoWithItem)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringOrderChoiceDto>> create(@Valid @RequestBody CateringOrderChoiceDto[] dtos,
                                                                        @RequestParam long reservationId) {

        final List<CateringOrderChoice> result = cateringOrderChoiceService.create(dtos, reservationId);
        return ResponseEntity.ok(result.stream()
                .map(CateringOrderChoiceMapper::toDto)
                .collect(ImmutableList.toImmutableList()));

    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringOrderChoiceDto> edit(@Valid @RequestBody CateringOrderChoiceDto dto,
                                                       @RequestParam long orderChoiceId) {

        final CateringOrderChoice orderChoice = cateringOrderChoiceService.edit(dto, orderChoiceId);
        return ResponseEntity.ok(CateringOrderChoiceMapper.toDtoWithItem(orderChoice));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam long id) {
        cateringOrderChoiceService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
