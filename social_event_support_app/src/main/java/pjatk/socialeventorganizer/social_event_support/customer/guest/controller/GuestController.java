package pjatk.socialeventorganizer.social_event_support.customer.guest.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.customer.guest.mapper.GuestMapper;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto;
import pjatk.socialeventorganizer.social_event_support.customer.guest.service.GuestService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/guests")
public class GuestController {

    private GuestService guestService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<GuestDto>> listGuests(@RequestParam(required = false) String keyword,
                                                              @RequestParam(defaultValue = "0") Integer pageNo,
                                                              @RequestParam(defaultValue = "50") Integer pageSize,
                                                              @RequestParam(defaultValue = "id") String sortBy,
                                                              @RequestParam(defaultValue = "asc") String order)  {
        log.info("GET GUESTS");
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build();

        final ImmutableList<Guest> list = guestService.list(customPage, keyword);

        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream().map(GuestMapper::toDto).collect(Collectors.toList())));
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "customer",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<GuestDto>> listGuestsByCustomerId(@RequestParam long customerId) {

        final List<Guest> guests = guestService.listAllByCustomerId(customerId);
        return ResponseEntity.ok(
                ImmutableList.copyOf(guests.stream()
                        .map(GuestMapper::toDto)
                        .collect(Collectors.toList()))
        );
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GuestDto> get(@RequestParam long id) {

        final Guest guest = guestService.get(id);
        return ResponseEntity.ok(GuestMapper.toDto(guest));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GuestDto> create(@RequestParam long customerId, @Valid @RequestBody GuestDto dto) {
        log.info("CREATE GUEST");
        final Guest guest = guestService.create(customerId, dto);
        return ResponseEntity.ok(GuestMapper.toDtoWithCustomer(guest));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam long customerId, @RequestParam long id) {
        guestService.delete(customerId, id);
        return ResponseEntity.noContent().build();
    }
}
