package pjatk.socialeventorganizer.social_event_support.customer.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service.CateringForChosenEventLocationService;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto;
import pjatk.socialeventorganizer.social_event_support.customer.guest.service.GuestService;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.initial_booking.EventBookDateDto;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.initial_booking.InitialEventBookingDto;
import pjatk.socialeventorganizer.social_event_support.event.service.EventTypeService;
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService;
import pjatk.socialeventorganizer.social_event_support.exceptions.ForbiddenAccessException;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.locationforevent.service.LocationForEventService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.user.service.EmailService;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.IN_PROGRESS;
import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.READY;
import static pjatk.socialeventorganizer.social_event_support.locationforevent.enums.ConfirmationStatusEnum.CONFIRMED;
import static pjatk.socialeventorganizer.social_event_support.locationforevent.enums.ConfirmationStatusEnum.NOT_CONFIRMED;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final SecurityService securityService;

    private final UserService userService;

    private final EmailService emailService;

    private final AddressService addressService;

    private final GuestService guestService;

    private final OrganizedEventService organizedEventService;

    private final LocationForEventService locationForEventService;

    private final CateringForChosenEventLocationService cateringForChosenEventLocationService;

    private final LocationService locationService;

    private final CateringService cateringService;

    private final OptionalServiceService optionalServiceService;

    private final EventTypeService eventTypeService;

    public ImmutableList<Customer> list(CustomPage customPagination, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageRequest.of(customPagination.getFirstResult(), customPagination.getMaxResult(),
                Sort.by(customPagination.getSort()).descending());
        final Page<Customer> page = customerRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }


    @Transactional
    public Customer create(CustomerDto dto) {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        if (!userService.isNewAccount(userCredentials.getUserId(), userCredentials.getUserType())) {
            throw new ForbiddenAccessException("Cannot access");
        }
        final AddressDto addressDto = dto.getAddress();
        final Address address = AddressMapper.fromDto(addressDto);
        addressService.save(address);

//        final User userById = userService.getUserById(userCredentials.getUserId());

        final Customer customer = CustomerMapper.fromDto(dto);
        customer.setAddress(address);

        log.info("TRYING TO SAVE CUSTOMER");
        customerRepository.save(customer);

        return customer;

    }

    public Customer getWithDetail(long id) {
        final Optional<Customer> optionalCustomer = customerRepository.getWithDetail(id);
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();
        }
        throw new NotFoundException("Customer with id " + id + " DOES NOT EXIST");

    }


    public CustomerDto getWithGuests(long id) {
        final Optional<Customer> optionalCustomer = customerRepository.getByIdWithAllGuests(id);
        if (optionalCustomer.isPresent()) {
            final Customer customer = optionalCustomer.get();
            return CustomerMapper.toDtoWithGuests(customer);
        }
        throw new NotFoundException("Customer with id " + id + " DOES NOT EXIST");

    }

    public CustomerDto getWithProblems(long id) {
        final Optional<Customer> optionalCustomer = customerRepository.getByIdWithProblems(id);
        if (optionalCustomer.isPresent()) {
            final Customer customer = optionalCustomer.get();
            return CustomerMapper.toDtoWithProblems(customer);
        }
        throw new NotFoundException("Customer with id " + id + " DOES NOT EXIST");

    }

    public Customer getWithAllEvents(long id) {
        final Optional<Customer> optionalCustomer = customerRepository.getByIdWithEvents(id);
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();
        }
        throw new NotFoundException("Customer with id " + id + " DOES NOT EXIST");

    }

    public Customer get(long id) {
        final Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();

        }
        throw new NotFoundException("Customer with id " + id + " DOES NOT EXIST");
    }

    //TODO:FINISH
    public void delete(long id) {
        //reservation set deletedAt
        //delete guests
        //set deletedAt
    }

    public Customer edit(CustomerDto dto, long id) {
        final Customer customer = get(id);

        customer.setBirthdate(DateTimeUtil.fromStringToFormattedDate(dto.getBirthdate()));
        customer.setLastName(dto.getLastName());
        customer.setFirstName(dto.getFirstName());
        customer.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()));
        customer.getUser().setModifiedAt(LocalDateTime.now());

        save(customer);

        return customer;
    }

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    //TODO:REFACTOR
    public Customer getWithEventByEventId(long id, long eventId) {

        return null;
    }

    public void addGuestsToEvent(long id, long eventId, long locationId, long[] guestIds) {

//        Optional<Customer> customerRepository.getById(id);
        final List<Guest> guests = guestService.getGuestsByIds(Arrays.asList(ArrayUtils.toObject(guestIds)));
        final LocationForEvent locationForEvent = locationForEventService.findByLocationIdAndEventId(locationId, eventId);

        if (!locationForEvent.getConfirmationStatus().equals(CONFIRMED.toString())) {
            throw new IllegalArgumentException("Cannot invite guests while reservation for location is not confirmed");
        }

        locationForEvent.setGuests(new HashSet<>(guests));

        locationForEventService.save(locationForEvent);

        final OrganizedEvent organizedEvent = organizedEventService.get(eventId);
        organizedEvent.setModifiedAt(LocalDateTime.now());

        organizedEventService.save(organizedEvent);

    }


    @Transactional
    public void sendInvitationToGuest(long eventId, long id) {
        final OrganizedEvent event = organizedEventService.get(eventId);

        if (!event.getEventStatus().equals(READY.toString())) {
            throw new IllegalArgumentException("Cannot send invitations while event status is not READY");
        }

        final OrganizedEvent organizedEvent = organizedEventService.getWithAllInformationForSendingInvitations(id);

        final OrganizedEventDto invitationContent = createInvitationContent(organizedEvent);

        final List<GuestDto> guests = invitationContent.getLocations().get(0).getGuests();


//        final Customer customer = get(id);
//
//        final OrganizerInfoResponse organizerInfo = OrganizerInfoMapper.mapToDto(customer);
//
//        final List<GuestInfoResponse> guestsInfo = guestService.getGuestsByOrganizedEventId(eventId);
//
//        final EventInfoResponse eventInfo = OrganizedEventMapper.mapToResponse(organizedEvent);
//
//        final List<LocationForEvent> locationForEvent = locationForEventService.getLocationInfoByOrganizedEventId(eventId);
//
//        final List<LocationInfoResponse> locationsInfo = locationForEvent.stream()
//                .map(LocationInfoMapper::mapToResponse)
//                .collect(Collectors.toList());
//
//        for (LocationInfoResponse locationInfoResponse : locationsInfo) {
//            final List<CateringPlaceInfoResponse> cateringList =
//                    cateringForChosenEventLocationService.getCateringForLocationInfoByOrganizedEventIdAndLocationId(eventId, locationInfoResponse.getLocationId());
//            locationInfoResponse.setCateringOrders(cateringList);
//        }
//
//        final InviteDto inviteDto = createInviteContent(organizerInfo, guestsInfo, eventInfo, locationsInfo);
//
//        for (GuestInfoResponse guest : guestsInfo) {
//            final String emailContent = ComposeInviteEmailUtil.composeEmail(guest, inviteDto);
//            final String emailSubject = "Invitation From " + organizerInfo.getFirstAndLastName();
//            final SimpleMailMessage inviteEmail = EmailUtil.emailBuilder(emailContent, guest.getEmail(), emailSubject);
//
//            log.info("EMAIL: " + inviteEmail.toString());
//
//            emailService.sendEmail(inviteEmail);
    }


    private OrganizedEventDto createInvitationContent(OrganizedEvent organizedEvent) {
        return OrganizedEventMapper.toDtoForInvite(organizedEvent);
    }



    @Transactional
    public LocationForEvent bookEvent(long id, long locId, InitialEventBookingDto dto) {

        final Customer customer = get(id);
        final Location location = locationService.getWithAvailability(locId, dto.getDetails().getDate());
        final EventBookDateDto details = dto.getDetails();

        locationService.modifyAvailabilityAfterBooking(location, details);

        final OrganizedEvent organizedEvent = OrganizedEvent.builder()
                .customer(customer)
                .name(dto.getName())
                .eventType(eventTypeService.getByType(dto.getEventType()))
                .eventStatus(IN_PROGRESS.toString())
                .isPredefined(false)
                .startDate(DateTimeUtil.fromStringToFormattedDate(details.getDate()))
                .endDate(DateTimeUtil.fromStringToFormattedDate(details.getDate()))
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        organizedEventService.save(organizedEvent);

        final LocationForEvent locationForEvent = LocationForEvent.builder()
                .location(location)
                .guestCount(details.getGuests())
                .dateTimeFrom(DateTimeUtil.fromStringToFormattedDateTime(details.getStartTime()))
                .dateTimeTo(DateTimeUtil.fromStringToFormattedDateTime(details.getEndTime()))
                .confirmationStatus(NOT_CONFIRMED.toString())
                .event(organizedEvent)
                .build();

        locationForEventService.save(locationForEvent);

        return locationForEvent;
    }
}
