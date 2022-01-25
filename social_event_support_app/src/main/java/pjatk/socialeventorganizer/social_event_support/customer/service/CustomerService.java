package pjatk.socialeventorganizer.social_event_support.customer.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper;
import pjatk.socialeventorganizer.social_event_support.common.mapper.PageableMapper;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.common.util.CollectionUtil;
import pjatk.socialeventorganizer.social_event_support.common.util.ComposeInviteEmailUtil;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.common.util.EmailUtil;
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
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService;
import pjatk.socialeventorganizer.social_event_support.exceptions.ActionNotAllowedException;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.exceptions.UserExistsException;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.service.LocationForEventService;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;
import pjatk.socialeventorganizer.social_event_support.security.password.PasswordEncoderSecurity;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.CustomerUserRegistrationDto;
import pjatk.socialeventorganizer.social_event_support.user.service.EmailService;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.CONFIRMED;
import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.NOT_CONFIRMED;
import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.*;
import static pjatk.socialeventorganizer.social_event_support.exceptions.UserExistsException.ENUM.USER_EXISTS;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final UserService userService;
    private final EmailService emailService;
    private final GuestService guestService;
    private final OrganizedEventService organizedEventService;
    private final LocationForEventService locationForEventService;
    private final CustomerAvatarService customerAvatarService;
    private final LocationService locationService;
    private final CateringService cateringService;
    private final OptionalServiceService optionalServiceService;
    private final TimestampHelper timestampHelper;
    private final PasswordEncoderSecurity passwordEncoderSecurity;

    public ImmutableList<Customer> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<Customer> page = customerRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    @Transactional(rollbackOn = Exception.class)
    public Customer createCustomerAccount(CustomerUserRegistrationDto dto) {
        if (userService.userExists(dto.getEmail())) {
            throw new UserExistsException(USER_EXISTS);
        }
        final Customer customer = CustomerMapper.fromCustomerRegistrationDto(dto);

        final String hashedPassword = passwordEncoderSecurity.bcryptEncryptor(dto.getPassword());
        customer.setPassword(hashedPassword);

        customer.setCreatedAt(timestampHelper.now());
        customer.setModifiedAt(timestampHelper.now());

        log.info("TRYING TO SAVE CUSTOMER");
        customerRepository.save(customer);

        return customer;
    }

    public <T> void sendMessage(long customerId, long receiverId, MessageDto messageDto, Class<T> clazz) {
        final User user = userService.get(customerId);
        final Customer customer = get(customerId);

        String className = clazz.getName();
        className = className.substring(className.lastIndexOf(".") + 1);

        switch (className) {
            case "Location":
                final Location location = locationService.getWithDetail(receiverId);
                messageDto.setReceiverEmail(location.getBusiness().getEmail());
                break;

            case "Catering":
                final Catering catering = cateringService.getWithDetail(receiverId);
                messageDto.setReceiverEmail(catering.getBusiness().getEmail());
                break;

            case "OptionalService":
                final OptionalService optionalService = optionalServiceService.getWithDetail(receiverId);
                messageDto.setReceiverEmail(optionalService.getBusiness().getEmail());
                break;

            default:
                throw new IllegalArgumentException("Incorrect receiver type");
        }
        messageDto.setReplyToEmail(customer.getEmail());
        final String content = "Message send from user " +
                customer.getFirstName() + " " +
                customer.getLastName() + " " +
                "with email" + " " + user.getEmail() +
                "\n\n" + messageDto.getContent() + "\n\nSent via SocialEventOrganizer app";

        final SimpleMailMessage inviteEmail = EmailUtil.buildEmail(content,
                messageDto.getReceiverEmail(), messageDto.getSubject(), messageDto.getReplyToEmail());

        emailService.sendEmail(inviteEmail);

    }

    public Customer getWithDetail(long id) {
        return customerRepository.getWithDetail(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " DOES NOT EXIST"));
    }

    public CustomerDto getWithGuests(long id) {
        return customerRepository.getByIdWithAllGuests(id)
                .map(CustomerMapper::toDtoWithGuests)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " DOES NOT EXIST"));
    }

    public CustomerDto getWithProblems(long id) {
        return customerRepository.getByIdWithProblems(id)
                .map(CustomerMapper::toDtoWithProblems)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " DOES NOT EXIST"));
    }

    public Customer getWithAllEvents(long id) {
        return customerRepository.getWithDetail(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " DOES NOT EXIST"));
    }

    public Customer get(long id) {
        return customerRepository.getByIdWithUser(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " DOES NOT EXIST"));
    }

    private boolean customerExists(long id) {
        return customerRepository.findById(id).isPresent();
    }

    public void delete(long id) {
        final Customer customerToDelete = customerRepository.getAllCustomerInformation(id)
                .orElseThrow(() -> new NotFoundException("Location with id " + id + " DOES NOT EXIST"));

        boolean hasPendingReservations = hasPendingReservations(customerToDelete);
        if (hasPendingReservations) {
            throw new ActionNotAllowedException("Cannot delete customer with reservations pending");
        }

        CollectionUtil.emptyListIfNull(customerToDelete.getGuests())
                .forEach(guestService::delete);

        CollectionUtil.emptyListIfNull(customerToDelete.getEvents())
                .forEach(organizedEventService::delete);

        customerAvatarService.delete(customerToDelete.getAvatar());

        customerRepository.save(customerToDelete);
    }


    public Customer edit(CustomerDto dto, long id) {
        final Customer customer = get(id);

        customer.setBirthdate(DateTimeUtil.fromStringToFormattedDate(dto.getBirthdate()));
        customer.setLastName(dto.getLastName());
        customer.setFirstName(dto.getFirstName());
        customer.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()));
        customer.setModifiedAt(timestampHelper.now());

        customerRepository.save(customer);

        return customer;
    }

    public void addGuestsToEvent(long id, long eventId, long[] guestIds) {
        if (!customerExists(id)) {
            throw new NotFoundException("No customer with id " + id);
        }
        final LocationForEvent locationForEvent = locationForEventService.findByEventId(eventId);

        if (!CONFIRMED.name().equals(locationForEvent.getConfirmationStatus())) {
            throw new IllegalArgumentException("Cannot invite guests while reservation for location is not confirmed");
        }

        final List<Guest> guests = guestService.getGuestsByIds(Arrays.asList(ArrayUtils.toObject(guestIds)));
        final OrganizedEvent organizedEvent = organizedEventService.get(eventId);

        organizedEvent.setGuests(new HashSet<>(guests));

        organizedEvent.setModifiedAt(timestampHelper.now());

        organizedEventService.save(organizedEvent);
    }

    @Transactional(rollbackOn = Exception.class)
    public void sendInvitationToGuest(long eventId, long customerId) {
        final OrganizedEvent organizedEvent = organizedEventService.getWithAllInformationForSendingInvitations(eventId, customerId);

        final Set<LocationForEvent> locationForEventSet = organizedEvent.getLocationForEvent();

        if (CollectionUtils.isEmpty(locationForEventSet)) {
            throw new ActionNotAllowedException("Cannot send invitations if no location booked");
        }

        final LocationForEvent locationForEvent = locationForEventSet.stream()
                .filter(location -> !CANCELLED.name().equals(location.getConfirmationStatus()))
                .findFirst()
                .orElseThrow(() -> new ActionNotAllowedException("No actual location reservation"));

        if (NOT_CONFIRMED.name().equals(locationForEvent.getConfirmationStatus())) {
            throw new ActionNotAllowedException("Cannot send invitations while location reservation not confirmed");
        }

        final Set<CateringForChosenEventLocation> cateringsForEventLocation = locationForEvent.getCateringsForEventLocation();

        if (!CollectionUtils.isEmpty(cateringsForEventLocation) && !cateringReservationsConfirmed(cateringsForEventLocation)) {
            throw new ActionNotAllowedException("Cannot send invitations while catering reservation not confirmed");
        }

        final Set<OptionalServiceForChosenLocation> services = locationForEvent.getServices();

        if (!CollectionUtils.isEmpty(services) && !servicesReservationsConfirmed(services)) {
            throw new ActionNotAllowedException("Cannot send invitations while services reservation not confirmed");
        }

        final OrganizedEventDto invitationContent = OrganizedEventMapper.toDtoForInvite(organizedEvent);

        final List<GuestDto> guests = invitationContent.getGuests();

        if (CollectionUtils.isEmpty(guests)) {
            throw new IllegalArgumentException("There are no guests invited to the event");
        }

        for (GuestDto guest : guests) {
            final String emailContent = ComposeInviteEmailUtil.composeEmail(guest, invitationContent);
            final String emailSubject = "Invitation From " + invitationContent.getCustomer().getFirstName() +
                    " " + invitationContent.getCustomer().getLastName();
            final SimpleMailMessage inviteEmail = EmailUtil.buildEmail(emailContent, guest.getEmail(), emailSubject, null);

            emailService.sendEmail(inviteEmail);
        }

        organizedEvent.setEventStatus(READY.name());
        organizedEventService.save(organizedEvent);
    }

    private boolean servicesReservationsConfirmed(Set<OptionalServiceForChosenLocation> services) {
        return services.stream()
                .allMatch(catering -> CONFIRMED.name().equals(catering.getConfirmationStatus()));
    }

    private boolean cateringReservationsConfirmed(Set<CateringForChosenEventLocation> cateringsForEventLocation) {
        return cateringsForEventLocation.stream()
                .allMatch(catering -> catering.isCateringOrderConfirmed() && CONFIRMED.name().equals(catering.getConfirmationStatus()));
    }

    private boolean hasPendingReservations(Customer customerToDelete) {
        return customerToDelete.getEvents().stream()
                .anyMatch(organizedEvent ->
                        !FINISHED.name().equals(organizedEvent.getEventStatus())
                                || !CANCELLED.name().equals(organizedEvent.getEventStatus()));

    }
}
