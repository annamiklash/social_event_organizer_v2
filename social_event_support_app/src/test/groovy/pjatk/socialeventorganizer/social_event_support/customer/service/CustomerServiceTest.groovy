package pjatk.socialeventorganizer.social_event_support.customer.service

import com.google.common.collect.ImmutableList
import org.springframework.data.domain.PageImpl
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper
import pjatk.socialeventorganizer.social_event_support.common.util.ComposeInviteEmailUtil
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil
import pjatk.socialeventorganizer.social_event_support.common.util.EmailUtil
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.CustomerAvatar
import pjatk.socialeventorganizer.social_event_support.customer.avatar.service.CustomerAvatarService
import pjatk.socialeventorganizer.social_event_support.customer.guest.service.GuestService
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper
import pjatk.socialeventorganizer.social_event_support.customer.message.dto.MessageDto
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper
import pjatk.socialeventorganizer.social_event_support.event.model.EventType
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.service.LocationForEventService
import pjatk.socialeventorganizer.social_event_support.location.model.Location
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService
import pjatk.socialeventorganizer.social_event_support.trait.address.AddressTrait
import pjatk.socialeventorganizer.social_event_support.trait.catering.CateringTrait
import pjatk.socialeventorganizer.social_event_support.trait.customer.CustomerTrait
import pjatk.socialeventorganizer.social_event_support.trait.customer.guest.GuestTrait
import pjatk.socialeventorganizer.social_event_support.trait.event.OrganizedEventTrait
import pjatk.socialeventorganizer.social_event_support.trait.location.LocationTrait
import pjatk.socialeventorganizer.social_event_support.trait.location.locationforevent.LocationForEventTrait
import pjatk.socialeventorganizer.social_event_support.trait.optional_service.OptionalServiceTrait
import pjatk.socialeventorganizer.social_event_support.trait.page.PageTrait
import pjatk.socialeventorganizer.social_event_support.trait.user.UserTrait
import pjatk.socialeventorganizer.social_event_support.user.service.EmailService
import pjatk.socialeventorganizer.social_event_support.user.service.UserService
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.time.LocalDateTime

import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.READY

class CustomerServiceTest extends Specification
        implements PageTrait,
                CustomerTrait,
                UserTrait,
                LocationTrait,
                CateringTrait,
                OptionalServiceTrait,
                OrganizedEventTrait,
                GuestTrait,
                LocationForEventTrait,
                AddressTrait {

    @Subject
    CustomerService customerService

    CustomerRepository customerRepository
    UserService userService
    EmailService emailService
    GuestService guestService
    OrganizedEventService organizedEventService
    LocationForEventService locationForEventService
    CustomerAvatarService customerAvatarService
    LocationService locationService
    CateringService cateringService
    OptionalServiceService optionalServiceService
    TimestampHelper timestampHelper

    LocalDateTime now

    def setup() {
        customerRepository = Mock()
        userService = Mock()
        emailService = Mock()
        guestService = Mock()
        organizedEventService = Mock()
        locationForEventService = Mock()
        customerAvatarService = Mock()
        locationService = Mock()
        cateringService = Mock()
        optionalServiceService = Mock()
        timestampHelper = Mock()

        now = LocalDateTime.parse('2007-12-03T10:15:30')
        timestampHelper.now() >> now

        customerService = new CustomerService(
                customerRepository,
                userService,
                emailService,
                guestService,
                organizedEventService,
                locationForEventService,
                customerAvatarService,
                locationService,
                cateringService,
                optionalServiceService,
                timestampHelper
        )
    }

    def "List"() {
        given:
        def keyword = 'keyword'
        def customPagination = fakePage

        def paging = fakePaging
        def page = new PageImpl<>([fakeCustomer])

        def target = ImmutableList.of(fakeCustomer)
        when:
        def result = customerService.list(customPagination, keyword)

        then:
        1 * customerRepository.findAllWithKeyword(paging, keyword) >> page

        result == target
    }

    def "Create"() {
        given:
        def dto = fakeCustomerDTO
        def customer = CustomerMapper.fromDto(dto)

        def avatar = CustomerAvatar.builder().id(1L).build()
        def user = fakeUser

        customer.setAvatar(avatar)
        customer.setId(user.getId())
        customer.setUser(user)
        user.setActive(true)
        user.setModifiedAt(now)

        def target = customer
        when:
        def result = customerService.create(fakeCustomerDTO)

        then:
        1 * customerAvatarService.create(dto.getAvatar()) >> avatar
        1 * userService.get(dto.getUser().getId()) >> user
        1 * userService.save(user)
        1 * customerRepository.save(customer)

        result == target
    }

    @Unroll
    def "SendMessage(#clazz)"() {
        given:
        def customerId = 1L
        def receiverId = 2L
        def messageDto = MessageDto.builder()
                .subject("SAMPLE SUBJECT")
                .receiverEmail('email@email.com')
                .build()

        def user = fakeUser
        def customer = fakeCustomer

        def location = fakeLocation
        def catering = fakeCatering
        def optionalService = fakeOptionalService

        def content = "Message send from user " +
                customer.getFirstName() + " " +
                customer.getLastName() + " " +
                "with email" + " " + user.getEmail() +
                "\n\n" + messageDto.getContent()

        def inviteEmail = EmailUtil.buildEmail(content,
                messageDto.getReceiverEmail(), messageDto.getSubject())

        when:
        customerService.sendMessage(customerId, receiverId, messageDto, clazz)

        then:
        1 * userService.get(customerId) >> user
        1 * customerRepository.findById(customerId) >> Optional.of(customer)
        1 * emailService.sendEmail(inviteEmail)

        locationService.get(receiverId) >> location
        cateringService.get(receiverId) >> catering
        optionalServiceService.get(receiverId) >> optionalService

        where:
        clazz                 | _
        Location.class        | _
        Catering.class        | _
        OptionalService.class | _

    }

    def "GetWithDetail"() {
        given:
        def id = 1L
        def target = fakeCustomer

        when:
        def result = customerService.getWithDetail(id)

        then:
        1 * customerRepository.getWithDetail(id) >> Optional.of(target)

        result == target
    }

    def "GetWithGuests"() {
        given:
        def id = 1L
        def customer = fakeCustomer
        def target = CustomerMapper.toDtoWithGuests(customer)

        when:
        def result = customerService.getWithGuests(id)

        then:
        1 * customerRepository.getByIdWithAllGuests(id) >> Optional.of(customer)

        result == target
    }

    def "GetWithProblems"() {
        given:
        def id = 1L
        def customer = fakeCustomer
        def target = CustomerMapper.toDtoWithProblems(customer)

        when:
        def result = customerService.getWithProblems(id)

        then:
        1 * customerRepository.getByIdWithProblems(id) >> Optional.of(customer)

        result == target
    }

    def "GetWithAllEvents"() {
        given:
        def id = 1L
        def target = fakeCustomer

        when:
        def result = customerService.getWithAllEvents(id)

        then:
        1 * customerRepository.getByIdWithEvents(id) >> Optional.of(target)

        result == target
    }

    def "Get"() {
        given:
        def id = 1L
        def target = fakeCustomer

        when:
        def result = customerService.get(id)

        then:
        1 * customerRepository.findById(id) >> Optional.of(target)

        result == target
    }

    def "CustomerExists"() {
        given:
        def id = 1L
        def target = true

        when:
        def result = customerService.customerExists(1L)

        then:
        1 * customerRepository.findById(id) >> Optional.of(fakeCustomer)

        result == target
    }

    def "Delete"() {
        given:
        def id = 1L

        def customer = fakeCustomer

        when:
        customerService.delete(id)

        then:
        1 * customerRepository.getAllCustomerInformation(id) >> Optional.of(customer)
        1 * userService.delete(customer.getUser())
        1 * customerRepository.save(customer)
    }

    def "Edit"() {
        given:
        def dto = fakeCustomerDTO
        def id = 1L

        def target = fakeCustomer
        target.setBirthdate(DateTimeUtil.fromStringToFormattedDate(dto.getBirthdate()))
        target.setLastName(dto.getLastName())
        target.setFirstName(dto.getFirstName())
        target.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()))
        target.getUser().setModifiedAt(timestampHelper.now())

        when:
        def result = customerService.edit(dto, id)

        then:
        1 * customerRepository.findById(id) >> Optional.of(target)
        1 * customerRepository.save(target)

        result == target
    }

    def "AddGuestsToEvent"() {
        given:
        def id = 1L
        def eventId = 2l
        def locationId = 3l
        def guestIds = [1L] as Long[]

        def customer = fakeCustomer
        def locationForEvent = fakeLocationForEvent
        def guests = [fakeGuest]
        def organizedEvent = fakeOrganizedEvent
        organizedEvent.setGuests(new HashSet<>(guests))
        organizedEvent.setModifiedAt(now)

        when:
        customerService.addGuestsToEvent(id, eventId, locationId, guestIds)

        then:
        1 * customerRepository.findById(id) >> Optional.of(customer)
        1 * locationForEventService.findByLocationIdAndEventId(locationId, eventId) >> locationForEvent
        1 * guestService.getGuestsByIds([1L]) >> guests
        1 * organizedEventService.get(eventId) >> organizedEvent
        1 * organizedEventService.save(organizedEvent)
    }

    def "SendInvitationToGuest"() {
        given:
        def eventId = 1L
        def customerId = 2L

        def event = fakeOrganizedEvent
        event.setEventStatus(READY.name())
        def address = fakeAddress
        def location = fakeLocation
        location.setLocationAddress(address)
        def locationForEvent = fakeLocationForEvent
        locationForEvent.setEvent(fakeOrganizedEvent)
        locationForEvent.setLocation(location)
        def customer = fakeCustomer
        customer.setUser(fakeUser)

        def organizedEvent = fakeOrganizedEvent
        organizedEvent.setGuests(Set.of(fakeGuest))
        organizedEvent.setEventType(EventType.builder()
                .id(1l)
                .type("SAMPLE TYPE")
                .build())
        organizedEvent.setCustomer(customer)
        organizedEvent.setLocationForEvent(locationForEvent)

        def guest = fakeGuestDTO

        def invitationContent = OrganizedEventMapper.toDtoForInvite(organizedEvent)
        def emailContent = ComposeInviteEmailUtil.composeEmail(guest, invitationContent)
        def emailSubject = "Invitation From " + invitationContent.getCustomer().getFirstName() +
                " " + invitationContent.getCustomer().getLastName()
        def inviteEmail = EmailUtil.buildEmail(emailContent, guest.getEmail(), emailSubject)

        when:
        customerService.sendInvitationToGuest(eventId, customerId)

        then:
        1 * organizedEventService.get(eventId) >> event
        1 * organizedEventService.getWithAllInformationForSendingInvitations(eventId, customerId) >> organizedEvent
        1 * emailService.sendEmail(inviteEmail)
    }
}
