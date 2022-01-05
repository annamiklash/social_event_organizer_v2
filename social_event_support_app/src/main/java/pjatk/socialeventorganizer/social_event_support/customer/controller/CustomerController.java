package pjatk.socialeventorganizer.social_event_support.customer.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.customer.message.dto.MessageDto;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CustomerDto>> findAll(@RequestParam(required = false) String keyword,
                                                              @RequestParam(defaultValue = "0") Integer pageNo,
                                                              @RequestParam(defaultValue = "50") Integer pageSize,
                                                              @RequestParam(defaultValue = "id") String sortBy,
                                                              @RequestParam(defaultValue = "asc") String order)  {
        log.info("GET ALL CUSTOMERS");
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build();
        final ImmutableList<Customer> list = customerService.list(customPage, keyword);

        // TODO: add count like in pjatk.socialeventorganizer.social_event_support.location.controller.LocationController.list
        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(CustomerMapper::toDto)
                        .collect(Collectors.toList()))
        );
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            params = {"id"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> get(@RequestParam long id) {
        log.info("GET CUSTOMER");
        try {
            final Customer customer = customerService.get(id);

            return ResponseEntity.ok(CustomerMapper.toDto(customer));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getWithDetail(@PathVariable long id) {

        try {
            final Customer customer = customerService.getWithDetail(id);
            return ResponseEntity.ok(CustomerMapper.toDtoWithDetail(customer));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/guests",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getWithGuests(@PathVariable long id) {
        log.info("GET CUSTOMER GUESTS");
        try {
            return ResponseEntity.ok(customerService.getWithGuests(id));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/problems",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getWithProblems(@PathVariable long id) {
        log.info("GET CUSTOMER REPORTED PROBLEMS");
        try {
            return ResponseEntity.ok(customerService.getWithProblems(id));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/events/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getWithAllEvents(@PathVariable long id) {
        log.info("GET CUSTOMER ALL EVENTS");
        try {
            return ResponseEntity.ok(CustomerMapper.toDtoWithDetail(customerService.getWithAllEvents(id)));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.PUT,
            params = {"id"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> edit(@RequestBody @Valid CustomerDto dto,
                                            @RequestParam long id) {
        final Customer customer = customerService.edit(dto, id);

        return ResponseEntity.ok(CustomerMapper.toDto(customer));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            params = {"id"})
    public ResponseEntity<Void> delete(@RequestParam long id) {
        log.info("CREATE CUSTOMER ACCOUNT");
        customerService.delete(id);

        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "invite/send",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendScheduleToAllGuests(@RequestParam long id,
                                                        @RequestParam long eventId) {
        log.info("SEND INVITE TO GUEST");
        customerService.sendInvitationToGuest(eventId, id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "message/location/send",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendMessageLocation(@RequestParam long customerId,
                                                    @RequestParam long locationId, @Valid @RequestBody MessageDto dto) {
        log.info("SEND MESSAGE");
        customerService.sendMessage(customerId, locationId, dto, Location.class);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "message/catering/send",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendMessageCatering(@RequestParam long customerId,
                                                    @RequestParam long cateringId, @Valid @RequestBody MessageDto dto) {
        log.info("SEND MESSAGE");
        customerService.sendMessage(customerId, cateringId, dto, Catering.class);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "message/service/send",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendMessageService(@RequestParam long customerId,
                                                   @RequestParam long serviceId, @Valid @RequestBody MessageDto dto) {
        log.info("SEND MESSAGE");
        customerService.sendMessage(customerId, serviceId, dto, OptionalService.class);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "guests/invite")
    public ResponseEntity<Void> addGuestsToLocationEvent(@RequestParam long id, @RequestParam long eventId,
                                                         @RequestParam long locId,
                                                         @RequestParam long[] guestIds) {

        customerService.addGuestsToEvent(id, eventId, locId, guestIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

}


