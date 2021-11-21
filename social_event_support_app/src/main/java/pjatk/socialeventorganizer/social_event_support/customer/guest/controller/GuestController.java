package pjatk.socialeventorganizer.social_event_support.customer.guest.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.customer.guest.mapper.GuestMapper;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto;
import pjatk.socialeventorganizer.social_event_support.customer.guest.service.GuestService;

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
                                                              @RequestParam(defaultValue = "0") Integer firstResult,
                                                              @RequestParam(defaultValue = "50") Integer maxResult,
                                                              @RequestParam(defaultValue = "id") String sort,
                                                              @RequestParam(defaultValue = "desc") String order) {
        log.info("GET GUESTS");
        return ResponseEntity.ok(guestService.list(new CustomPage(maxResult, firstResult, sort, order), keyword));
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
            params = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GuestDto> get(@RequestParam long id) {

        final Guest guest = guestService.get(id);
        return ResponseEntity.ok(GuestMapper.toDto(guest));
    }
}
