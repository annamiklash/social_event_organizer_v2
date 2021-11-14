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
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto;
import pjatk.socialeventorganizer.social_event_support.customer.guest.service.GuestService;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventConfirmationDto;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.initial_booking.InitialEventBookingDto;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.dto.CateringReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.LocationReview;
import pjatk.socialeventorganizer.social_event_support.reviews.location_review.model.dto.LocationReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.OptionalServiceReview;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.dto.ServiceReviewDto;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/customers")
public class CustomerController {

    private final CustomerService customerService;

    private final GuestService guestService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CustomerDto>> findAll(@RequestParam(required = false) String keyword,
                                                              @RequestParam(defaultValue = "0") Integer firstResult,
                                                              @RequestParam(defaultValue = "50") Integer maxResult,
                                                              @RequestParam(defaultValue = "id") String sort,
                                                              @RequestParam(defaultValue = "desc") String order) {
        log.info("GET ALL CUSTOMERS");
        final ImmutableList<Customer> list = customerService.list(new CustomPage(maxResult, firstResult, sort, order), keyword);

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
            return ResponseEntity.ok(CustomerMapper.toDtoWithEvents(customerService.getWithAllEvents(id)));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "guests",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<GuestDto>> listGuests(@RequestParam(required = false) String keyword,
                                                              @RequestParam(defaultValue = "0") Integer firstResult,
                                                              @RequestParam(defaultValue = "50") Integer maxResult,
                                                              @RequestParam(defaultValue = "id") String sort,
                                                              @RequestParam(defaultValue = "desc") String order) {
        log.info("GET GUESTS");
        return ResponseEntity.ok(guestService.list(new CustomPage(maxResult, firstResult, sort, order), keyword));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> create(@RequestBody @Valid CustomerDto dto) {
        log.info("CREATE CUSTOMER ACCOUNT");
        customerService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.PUT,
            params = {"id"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> edit(@RequestBody @Valid CustomerDto dto,
                                            @RequestParam long id) {
        log.info("CREATE CUSTOMER ACCOUNT");
        customerService.edit(dto, id);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            params = {"id"})
    public ResponseEntity<Void> delete(@RequestParam long id) {
        log.info("CREATE CUSTOMER ACCOUNT");
        customerService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "invite",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendScheduleToAllGuests(@RequestParam long id,
                                                        @RequestParam long eventId) {
        log.info("SEND INVITE TO GUEST");
        customerService.sendInvitationToGuest(id, eventId);
        return ResponseEntity.ok().build();
    }

    //TODO: add verification if location/catering/service was booked
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "location",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationReviewDto> reviewLocation(@RequestParam long id,
                                                            @RequestParam long locationId,
                                                            @Valid @RequestBody LocationReviewDto dto) {

        final LocationReview review = customerService.leaveLocationReview(id, locationId, dto);
        return ResponseEntity.ok(ReviewMapper.toLocationReviewDto(review));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "catering",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringReviewDto> reviewCatering(@RequestParam long id,
                                                            @RequestParam long cateringId,
                                                            @Valid @RequestBody CateringReviewDto dto) {

        final CateringReview review = customerService.leaveCateringReview(id, cateringId, dto);
        return ResponseEntity.ok(ReviewMapper.toCateringReviewDto(review));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "service",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceReviewDto> reviewService(@RequestParam long id,
                                                          @RequestParam long serviceId,
                                                          @Valid @RequestBody ServiceReviewDto dto) {

        final OptionalServiceReview review = customerService.leaveServiceReview(id, serviceId, dto);
        return ResponseEntity.ok(ReviewMapper.toServiceReviewDto(review));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "book",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizedEventConfirmationDto> bookLocationForEvent(@RequestParam long id,
                                                                              @RequestParam long locId,
                                                                              @Valid @RequestBody InitialEventBookingDto dto) {

        final LocationForEvent locationForEvent = customerService.bookEvent(id, locId, dto);
        return ResponseEntity.ok(OrganizedEventMapper.toOrganizedConfirmationDto(locationForEvent));

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


