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
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper;
import pjatk.socialeventorganizer.social_event_support.common.mapper.PageableMapper;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.common.util.CollectionUtil;
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
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService;
import pjatk.socialeventorganizer.social_event_support.exceptions.ActionNotAllowedException;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.service.LocationForEventService;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.EmailService;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.CONFIRMED;
import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.*;

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


    public ImmutableList<Customer> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<Customer> page = customerRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    @Transactional(rollbackOn = Exception.class)
    public Customer create(CustomerDto dto) {
        final Customer customer = CustomerMapper.fromDto(dto);

        if (dto.getAvatar() != null) {
            final CustomerAvatar avatar = customerAvatarService.create(dto.getAvatar());
            customer.setAvatar(avatar);
        }
        final User user = userService.get(dto.getUser().getId());

        customer.setId(user.getId());
        customer.setUser(user);
        user.setActive(true);
        user.setModifiedAt(timestampHelper.now());

        userService.save(user);

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

        final String content = "Message send from user " +
                customer.getFirstName() + " " +
                customer.getLastName() + " " +
                "with email" + " " + user.getEmail() +
                "\n\n" + messageDto.getContent();

        final SimpleMailMessage inviteEmail = EmailUtil.buildEmail(content,
                messageDto.getReceiverEmail(), messageDto.getSubject());

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
        return customerRepository.getByIdWithEvents(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " DOES NOT EXIST"));
    }

    public Customer get(long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " DOES NOT EXIST"));
    }

    public boolean customerExists(long id) {
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
        userService.delete(customerToDelete.getUser());

        customerRepository.save(customerToDelete);
    }


    public Customer edit(CustomerDto dto, long id) {
        final Customer customer = get(id);

        customer.setBirthdate(DateTimeUtil.fromStringToFormattedDate(dto.getBirthdate()));
        customer.setLastName(dto.getLastName());
        customer.setFirstName(dto.getFirstName());
        customer.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()));
        customer.getUser().setModifiedAt(timestampHelper.now());

        customerRepository.save(customer);

        return customer;
    }

    public void addGuestsToEvent(long id, long eventId, long locationId, long[] guestIds) {
        if (!customerExists(id)) {
            throw new NotFoundException("No customer with id " + id);
        }
        final LocationForEvent locationForEvent = locationForEventService.findByLocationIdAndEventId(locationId, eventId);

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
        final OrganizedEvent event = organizedEventService.get(eventId);

        if (!READY.name().equals(event.getEventStatus())) {
            throw new IllegalArgumentException("Cannot send invitations while event status is not READY");
        }

        final OrganizedEvent organizedEvent = organizedEventService.getWithAllInformationForSendingInvitations(eventId, customerId);

        final OrganizedEventDto invitationContent = OrganizedEventMapper.toDtoForInvite(organizedEvent);

        final List<GuestDto> guests = invitationContent.getGuests();

        if (CollectionUtils.isEmpty(guests)) {
            throw new IllegalArgumentException("There are no guests invited to the event");
        }

        for (GuestDto guest : guests) {
            final String emailContent = ComposeInviteEmailUtil.composeEmail(guest, invitationContent);
            final String emailSubject = "Invitation From " + invitationContent.getCustomer().getFirstName() +
                    " " + invitationContent.getCustomer().getLastName();
            final SimpleMailMessage inviteEmail = EmailUtil.buildEmail(emailContent, guest.getEmail(), emailSubject);

            emailService.sendEmail(inviteEmail);
        }
    }

    private boolean hasPendingReservations(Customer customerToDelete) {
        return customerToDelete.getEvents().stream()
                .anyMatch(organizedEvent ->
                        !FINISHED.name().equals(organizedEvent.getEventStatus())
                                || !CANCELLED.name().equals(organizedEvent.getEventStatus()));

    }
}
