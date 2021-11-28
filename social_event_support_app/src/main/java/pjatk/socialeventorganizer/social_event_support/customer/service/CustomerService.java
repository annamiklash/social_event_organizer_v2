package pjatk.socialeventorganizer.social_event_support.customer.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service.CateringForChosenEventLocationService;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.common.util.ComposeInviteEmailUtil;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.common.util.EmailUtil;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.CustomerAvatar;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.service.CustomerAvatarService;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto;
import pjatk.socialeventorganizer.social_event_support.customer.guest.service.GuestService;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.customer.message.dto.MessageDto;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.event.service.EventTypeService;
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotAvailableException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.mapper.LocationForEventMapper;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto.LocationForEventDto;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.service.LocationForEventService;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.EmailService;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.READY;
import static pjatk.socialeventorganizer.social_event_support.location.locationforevent.enums.ConfirmationStatusEnum.CONFIRMED;

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

    private final CustomerAvatarService customerAvatarService;

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


    @Transactional(rollbackOn = Exception.class)
    public Customer create(CustomerDto dto) {

        final Address address = addressService.create(dto.getAddress());
        final Customer customer = CustomerMapper.fromDto(dto);

        if (dto.getAvatar() != null) {
            final CustomerAvatar avatar = customerAvatarService.create(dto.getAvatar());
            customer.setAvatar(avatar);
        }
        final User user = userService.getById(dto.getUser().getId());

        customer.setId(user.getId());
        customer.setUser(user);
        customer.setAddress(address);

        user.setActive(true);
        user.setModifiedAt(LocalDateTime.now());

        userService.save(user);

        log.info("TRYING TO SAVE CUSTOMER");
        customerRepository.save(customer);

        return customer;

    }

    public void sendMessage(long customerId, long receiverId, MessageDto messageDto, Class clazz) {
        final User user = userService.getById(customerId);
        final Customer customer = get(customerId);

        String className = clazz.getName();
        className = className.substring(className.lastIndexOf(".") + 1);

        switch (className) {
            case "Location":
                final Location location = locationService.get(receiverId);
                messageDto.setReceiverEmail(location.getEmail());
                break;

            case "Catering":
                final Catering catering = cateringService.get(receiverId);
                messageDto.setReceiverEmail(catering.getEmail());
                break;

            case "OptionalService":
                final OptionalService optionalService = optionalServiceService.get(receiverId);
                messageDto.setReceiverEmail(optionalService.getEmail());
                break;

            default:
                throw new IllegalArgumentException("Incorrect receiver type");
        }

        String content = messageDto.getContent();
        content = new StringBuilder()
                .append("Message send from user ")
                .append(customer.getFirstName()).append(" ").append(customer.getLastName()).append(" ")
                .append("with email").append(" ").append(user.getEmail())
                .append("\n\n")
                .append(content).toString();

        final SimpleMailMessage inviteEmail = EmailUtil.buildEmail(content,
                messageDto.getReceiverEmail(), messageDto.getSubject());

        emailService.sendEmail(inviteEmail);

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
        final LocationForEvent locationForEvent = locationForEventService.findByLocationIdAndEventId(locationId, eventId);

        if (!locationForEvent.getConfirmationStatus().equals(CONFIRMED.toString())) {
            throw new IllegalArgumentException("Cannot invite guests while reservation for location is not confirmed");
        }

        final List<Guest> guests = guestService.getGuestsByIds(Arrays.asList(ArrayUtils.toObject(guestIds)));
        final OrganizedEvent organizedEvent = organizedEventService.get(eventId);

        organizedEvent.setGuests(new HashSet<>(guests));

        organizedEvent.setModifiedAt(LocalDateTime.now());

        organizedEventService.save(organizedEvent);

    }

    @Transactional(rollbackOn = Exception.class)
    public void sendInvitationToGuest(long eventId, long customerId) {
        final OrganizedEvent event = organizedEventService.get(eventId);

        if (!event.getEventStatus().equals(READY.toString())) {
            throw new IllegalArgumentException("Cannot send invitations while event status is not READY");
        }

        final OrganizedEvent organizedEvent = organizedEventService.getWithAllInformationForSendingInvitations(eventId, customerId);

        final OrganizedEventDto invitationContent = createInvitationContent(organizedEvent);

        final List<GuestDto> guests = invitationContent.getGuests();

        if (CollectionUtils.isEmpty(guests)) {
            throw new IllegalArgumentException("There are no guests invited to the event");
        }

        for (GuestDto guest : guests) {
            final String emailContent = ComposeInviteEmailUtil.composeEmail(guest, invitationContent);
            final String emailSubject = "Invitation From " + invitationContent.getCustomer().getFirstName() + " " + invitationContent.getCustomer().getLastName();
            final SimpleMailMessage inviteEmail = EmailUtil.buildEmail(emailContent, guest.getEmail(), emailSubject);

            emailService.sendEmail(inviteEmail);
        }
    }


    private OrganizedEventDto createInvitationContent(OrganizedEvent organizedEvent) {
        return OrganizedEventMapper.toDtoForInvite(organizedEvent);
    }

    @Transactional(rollbackOn = Exception.class)
    public LocationForEvent bookInitialLocationForEvent(long id, long locId, long eventId, LocationForEventDto dto) {

        final OrganizedEvent organizedEvent = organizedEventService.get(eventId);
        final String date = DateTimeUtil.toDateOnlyStringFromLocalDateTime(organizedEvent.getStartDateTime().toLocalDate());
        final boolean isAvailable = locationService.isAvailable(locId, date, dto.getTimeFrom(), dto.getTimeTo());

        if (!isAvailable) {
            throw new NotAvailableException("Location not available for selected date and time");
        }
        final Location location = locationService.get(locId);

        locationService.modifyAvailabilityAfterBooking(location, date, dto.getTimeFrom(), dto.getTimeTo());

        final LocationForEvent locationForEvent = LocationForEventMapper.fromDto(dto);
        locationForEvent.setLocation(location);
        locationForEvent.setEvent(organizedEvent);

        locationForEventService.save(locationForEvent);

        return locationForEvent;
    }
}
