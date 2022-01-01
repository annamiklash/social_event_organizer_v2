package pjatk.socialeventorganizer.social_event_support.event.service

import com.google.common.collect.ImmutableList
import org.springframework.data.domain.PageImpl
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository
import pjatk.socialeventorganizer.social_event_support.enums.CustomerReservationTabEnum
import pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum
import pjatk.socialeventorganizer.social_event_support.event.helper.StatusChangeHelper
import pjatk.socialeventorganizer.social_event_support.event.model.EventType
import pjatk.socialeventorganizer.social_event_support.event.repository.OrganizedEventRepository
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException
import pjatk.socialeventorganizer.social_event_support.trait.customer.CustomerTrait
import pjatk.socialeventorganizer.social_event_support.trait.event.OrganizedEventTrait
import pjatk.socialeventorganizer.social_event_support.trait.page.PageTrait
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.time.LocalDateTime

class OrganizedEventServiceTest extends Specification
        implements PageTrait, OrganizedEventTrait, CustomerTrait {

    @Subject
    OrganizedEventService organizedEventService

    OrganizedEventRepository organizedEventRepository
    EventTypeService eventTypeService
    CustomerRepository customerRepository
    StatusChangeHelper statusChangeHelper
    TimestampHelper timestampHelper

    LocalDateTime now

    def setup() {
        organizedEventRepository = Mock()
        eventTypeService = Mock()
        customerRepository = Mock()
        statusChangeHelper = Mock()
        timestampHelper = Mock()

        now = LocalDateTime.parse('2007-12-03T10:15:30')
        timestampHelper.now() >> now

        organizedEventService = new OrganizedEventService(organizedEventRepository,
                eventTypeService,
                customerRepository,
                statusChangeHelper,
                timestampHelper
        )
    }


    def "List"() {
        given:
        def customPage = fakePage
        def keyword = "sample keyword"
        def paging = fakePaging
        def page = new PageImpl<>([fakeOrganizedEvent])
        def target = ImmutableList.of(fakeOrganizedEventDto)

        when:
        def result = organizedEventService.list(customPage, keyword)

        then:
        1 * organizedEventRepository.findAll(paging) >> page

        result == target
    }

    def "Get"() {
        given:
        def orgEventId = 1L
        def organizedEvent = fakeOrganizedEvent

        def target = organizedEvent
        when:
        def result = organizedEventService.get(orgEventId)

        then:
        1 * organizedEventRepository.findById(orgEventId) >> Optional.of(organizedEvent)

        result == target
    }

    def "Get notFoundException"() {
        given:
        def orgEventId = 1L
        when:
        organizedEventService.get(orgEventId)

        then:
        1 * organizedEventRepository.findById(orgEventId) >> Optional.empty()

        thrown(NotFoundException)
    }

    def "GetWithDetail"() {
        given:
        def orgEventId = 1L
        def organizedEvent = fakeOrganizedEvent

        def target = organizedEvent
        when:
        def result = organizedEventService.getWithDetail(orgEventId)

        then:
        1 * organizedEventRepository.getWithDetail(orgEventId) >> Optional.of(organizedEvent)

        result == target
    }

    def "GetWithDetail NotFoundException"() {
        given:
        def orgEventId = 1L
        when:
        organizedEventService.getWithDetail(orgEventId)

        then:
        1 * organizedEventRepository.getWithDetail(orgEventId) >> Optional.empty()

        thrown(NotFoundException)
    }

    def "Save"() {
        given:
        def organizedEvent = fakeOrganizedEvent

        when:
        organizedEventService.save(organizedEvent)

        then:
        1 * organizedEventRepository.save(organizedEvent)
    }

    def "GetWithAllInformationForSendingInvitations"() {
        given:
        def eventId = 1L
        def customerId = 1L
        def organizedEvent = fakeOrganizedEvent

        def target = organizedEvent

        when:
        def result = organizedEventService.getWithAllInformationForSendingInvitations(eventId, customerId)

        then:
        1 * organizedEventRepository.getWithAllInformationForSendingInvitations(eventId, customerId) >> Optional.of(organizedEvent)

        result == target
    }

    def "GetWithAllInformationForSendingInvitations NotFoundException"() {
        given:
        def eventId = 1L
        def customerId = 1L

        when:
        organizedEventService.getWithAllInformationForSendingInvitations(eventId, customerId)

        then:
        1 * organizedEventRepository.getWithAllInformationForSendingInvitations(eventId, customerId) >> Optional.empty()

        thrown(NotFoundException)
    }

    @Unroll
    def "ChangeStatus for status = #status"() {
        given:
        def customerId = 1L
        def eventId = 2L

        when:
        def result = organizedEventService.changeStatus(customerId, eventId, status)

        then:
        1 * organizedEventRepository.existsOrganizedEventByIdAndCustomer_Id(eventId, customerId) >> true
        1 * organizedEventRepository.getWithDetail(eventId) >> Optional.of(organizedEvent)
        1 * organizedEventRepository.save(organizedEvent)
        statusChangeHelper.possibleToChangeStatusFromInProgressToConfirmed(organizedEvent) >> true

        result == target

        where:
        status                      | organizedEvent                                    | target
        EventStatusEnum.IN_PROGRESS | fakeOrganizedEvent.withEventStatus('CONFIRMED')   | fakeOrganizedEvent.withEventStatus('IN_PROGRESS')
        EventStatusEnum.CONFIRMED   | fakeOrganizedEvent.withEventStatus('IN_PROGRESS') | fakeOrganizedEvent.withEventStatus('CONFIRMED')
        EventStatusEnum.READY       | fakeOrganizedEvent.withEventStatus('CONFIRMED')   | fakeOrganizedEvent.withEventStatus('READY')
        EventStatusEnum.CANCELLED   | fakeOrganizedEvent.withEventStatus('CONFIRMED')   | fakeOrganizedEvent.withEventStatus('CANCELLED')
        EventStatusEnum.FINISHED    | fakeOrganizedEvent.withEventStatus('CONFIRMED')   | fakeOrganizedEvent.withEventStatus('FINISHED')
    }

    @Unroll
    def "GetAllByCustomerIdAndTab for tabEnum = #tabEnum"() {
        given:
        def customerId = 1L
        def organizedEvent = fakeOrganizedEvent
        def organizedEventList = [organizedEvent]

        def target = organizedEventList

        when:
        def result = organizedEventService.getAllByCustomerIdAndTab(customerId, tabEnum)

        then:
        1 * customerRepository.existsById(customerId) >> true
        organizedEventRepository.findAllByCustomer_Id(customerId) >> organizedEventList
        organizedEventRepository.findAllFinished(customerId) >> organizedEventList
        organizedEventRepository.findAllCurrent(customerId) >> organizedEventList

        result == target

        where:
        tabEnum                            | _
        CustomerReservationTabEnum.ALL     | _
        CustomerReservationTabEnum.PAST    | _
        CustomerReservationTabEnum.CURRENT | _
    }

    def "GetWithLocation"() {
        given:
        def orgEventId = 1L
        def organizedEvent = fakeOrganizedEvent

        def target = organizedEvent
        when:
        def result = organizedEventService.getWithLocation(orgEventId)

        then:
        1 * organizedEventRepository.getWithLocation(orgEventId) >> Optional.of(organizedEvent)

        result == target
    }

    def "GetWithLocation NotFoundException"() {
        given:
        def orgEventId = 1L
        when:
        organizedEventService.getWithLocation(orgEventId)

        then:
        1 * organizedEventRepository.getWithLocation(orgEventId) >> Optional.empty()

        thrown(NotFoundException)
    }

    def "Create"() {
        given:
        def customerId = 1L
        def dto = fakeOrganizedEventDto
        def customer = fakeCustomer
        def eventType = EventType.builder().id(1L).build()

        def target = fakeOrganizedEvent
                .withId(null)
                .withEventType(eventType)
                .withCustomer(customer)
                .withCreatedAt(now)
                .withModifiedAt(now)
                .withLocationForEvent(null)

        when:
        def result = organizedEventService.create(customerId, dto)

        then:
        1 * customerRepository.findById(customerId) >> Optional.of(customer)
        1 * eventTypeService.getByType(dto.getEventType()) >> eventType
        1 * organizedEventRepository.save(target)

        result == target
    }

    def "Delete"() {
        given:
        def organizedEvent = fakeOrganizedEvent
        .withModifiedAt(now)
        .withDeletedAt(now)

        when:
        organizedEventService.delete(organizedEvent)

        then:
        1 * organizedEventRepository.save(organizedEvent)
    }
}
