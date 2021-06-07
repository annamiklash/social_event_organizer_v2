package pjatk.socialeventorganizer.social_event_support.customer.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.request.AddressRequest;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service.CateringForChosenEventLocationService;
import pjatk.socialeventorganizer.social_event_support.customer.guest.Guest;
import pjatk.socialeventorganizer.social_event_support.customer.guest.GuestService;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.model.request.CreateCustomerAccountRequest;
import pjatk.socialeventorganizer.social_event_support.customer.model.response.CustomerInformationResponse;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.event.dto.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService;
import pjatk.socialeventorganizer.social_event_support.exceptions.ForbiddenAccessException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.invite.InviteContent;
import pjatk.socialeventorganizer.social_event_support.invite.mapper.EventInfoMapper;
import pjatk.socialeventorganizer.social_event_support.invite.mapper.OrganizerInfoMapper;
import pjatk.socialeventorganizer.social_event_support.invite.response.*;
import pjatk.socialeventorganizer.social_event_support.locationforevent.service.LocationForEventService;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.EmailService;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;
import pjatk.socialeventorganizer.social_event_support.util.ComposeInviteEmailUtil;
import pjatk.socialeventorganizer.social_event_support.util.EmailUtil;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private final EventInfoMapper eventInfoMapper;

    private final CustomerMapper customerMapper;

    private final AddressMapper addressMapper;

    private final OrganizerInfoMapper organizerInfoMapper;


    public ImmutableList<Customer> findAll() {
        final List<Customer> customerList = customerRepository.findAll();
        return ImmutableList.copyOf(customerList);
    }

    @Transactional
    public void createCustomerAccount(CreateCustomerAccountRequest customerRequest) {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        if (!userCredentials.getIsNewAccount()) {
            throw new ForbiddenAccessException("Cannot access");
        }
        final AddressRequest addressRequest = customerRequest.getAddressRequest();
        final Address address = addressMapper.mapToDTO(addressRequest);
        addressService.save(address);

        final User userById = userService.getUserById(userCredentials.getUserId());

        final Customer customer = customerMapper.mapToDTO(customerRequest, userById);
        customer.setAddress(address);

        log.info("TRYING TO SAVE CUSTOMER");
        customerRepository.save(customer);

        userCredentials.setIsNewAccount(false);
    }

    public CustomerInformationResponse getCustomerInformation() {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        final Optional<Customer> optionalCustomer = customerRepository.findByUser_Id(userCredentials.getUserId());
        if (optionalCustomer.isPresent()) {
            final Customer customer = optionalCustomer.get();
            return customerMapper.mapToCustomerInformationResponse(customer);
        }
        throw new NotFoundException("Cannot find customer with id " + userCredentials.getUserId());

    }

    public Optional<Set<Guest>> findAllCustomerGuests() {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        final Optional<Customer> optionalCustomer = customerRepository.findByUser_Id(userCredentials.getUserId());
        if (optionalCustomer.isPresent()) {
            final Customer customer = optionalCustomer.get();
            return Optional.of(customer.getGuests());
        }
        throw new NotFoundException("Cannot find customer with id " + userCredentials.getUserId());

    }

    @Transactional
    public void sendInvitationToGuest(long orgEventId) {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        final Optional<Customer> optionalCustomer = customerRepository.findByUser_Id(userCredentials.getUserId());
        if (!optionalCustomer.isPresent()) {
            throw new NotFoundException("Cannot find customer with id " + userCredentials.getUserId());
        }
        final Customer customer = optionalCustomer.get();

        final OrganizerInfoResponse organizerInfo = organizerInfoMapper.mapToResponse(customer);

        final List<GuestInfoResponse> guestsInfo = guestService.getGuestsByOrganizedEventId(orgEventId);

        final OrganizedEvent organizedEvent = organizedEventService.getByOrganizedEventId(orgEventId);

        final EventInfoResponse eventInfo = eventInfoMapper.mapToResponse(organizedEvent);

        final List<LocationInfoResponse> locationsInfo = locationForEventService.getLocationInfoByOrganizedEventId(orgEventId);

        for (LocationInfoResponse locationInfoResponse : locationsInfo) {
            final List<CateringPlaceInfoResponse> cateringList =
                    cateringForChosenEventLocationService.getCateringForLocationInfoByOrganizedEventIdAndLocationId(orgEventId, locationInfoResponse.getLocationId());
            locationInfoResponse.setCateringOrders(cateringList);
        }

        final InviteContent inviteContent = createInviteContent(organizerInfo, guestsInfo, eventInfo, locationsInfo);

        for (GuestInfoResponse guest : guestsInfo) {
            final String emailContent = ComposeInviteEmailUtil.composeEmail(guest, inviteContent);
            final String emailSubject = "Invitation From " + organizerInfo.getFirstAndLastName();
            final SimpleMailMessage inviteEmail = EmailUtil.emailBuilder(emailContent, guest.getEmail(), emailSubject);

            log.info("EMAIL: " + inviteEmail.toString());

            emailService.sendEmail(inviteEmail);
        }
    }

    private InviteContent createInviteContent(OrganizerInfoResponse organizerInfo,
                                              List<GuestInfoResponse> guestsInfo,
                                              EventInfoResponse eventInfo,
                                              List<LocationInfoResponse> locationsInfo) {
        return InviteContent.builder()
                .organizerInfo(organizerInfo)
                .guestInfo(guestsInfo)
                .eventInfo(eventInfo)
                .locationInfo(locationsInfo)
                .build();
    }


}
