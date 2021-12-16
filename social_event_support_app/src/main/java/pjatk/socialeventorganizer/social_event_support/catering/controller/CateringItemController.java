package pjatk.socialeventorganizer.social_event_support.catering.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringItemMapper;
import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringItemDto;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringItemService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/catering/items")
public class CateringItemController {

    private final CateringItemService cateringItemService;

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @RequestMapping(
//            path = "new",
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<TableDto<CateringItemDto>> findAll(@RequestParam(required = false) String keyword,
//                                                             @RequestParam(defaultValue = "0") Integer pageNo,
//                                                             @RequestParam(defaultValue = "5") Integer pageSize,
//                                                             @RequestParam(defaultValue = "id") String sortBy) {
//        final ImmutableList<CateringItem> list = cateringItemService.list(
//                CustomPage.builder()
//                        .pageNo(pageNo)
//                        .pageSize(pageSize)
//                        .sortBy(sortBy).build(), keyword);
//
//        final ImmutableList<CateringItemDto> result = ImmutableList.copyOf(list.stream()
//                .map(CateringItemMapper::toDto)
//                .collect(Collectors.toList()));
//
//        return ResponseEntity.ok(new TableDto<>(TableDto.MetaDto.builder().pageNo(pageNo).pageSize(pageSize).sortBy(sortBy).build(), result));
//    }

    @RequestMapping(
            path = "allowed",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringItemDto>> listAllByCateringId(@RequestParam long cateringId) {
        log.info("GET ALL CATERING_ITEM_TYPES");

        final ImmutableList<CateringItem> list = cateringItemService.listAllByCateringId(cateringId);

        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(CateringItemMapper::toDto)
                        .collect(Collectors.toList())));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringItemDto> get(@RequestParam long id) {

        final CateringItem item = cateringItemService.get(id);

        return ResponseEntity.ok(CateringItemMapper.toDto(item));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringItemDto> create(@Valid @RequestBody CateringItemDto dto, @RequestParam long cateringId) {

        final CateringItem cateringItem = cateringItemService.create(dto, cateringId);
        return ResponseEntity.ok(CateringItemMapper.toDto(cateringItem));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringItemDto> edit(@Valid @RequestBody CateringItemDto dto, @RequestParam long id) {
        final CateringItem cateringItem = cateringItemService.edit(id, dto);
        return ResponseEntity.ok(CateringItemMapper.toDto(cateringItem));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam long id) {
        cateringItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
