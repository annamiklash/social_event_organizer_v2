package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service


import com.google.common.collect.ImmutableSet
import org.codehaus.groovy.runtime.InvokerHelper
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper.CateringForChosenLocationMapper
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.repository.CateringForLocationRepository
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository
import pjatk.socialeventorganizer.social_event_support.event.repository.OrganizedEventRepository
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException
import pjatk.socialeventorganizer.social_event_support.trait.BusinessHoursTrait
import pjatk.socialeventorganizer.social_event_support.trait.catering.CateringTrait
import pjatk.socialeventorganizer.social_event_support.trait.cateringforchosenevent.CateringForChosenEventLocationTrait
import pjatk.socialeventorganizer.social_event_support.trait.event.OrganizedEventTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.*

class CateringForChosenEventLocationServiceTest extends Specification
        implements CateringForChosenEventLocationTrait,
                OrganizedEventTrait,
                BusinessHoursTrait,
                CateringTrait {

    @Subject
    CateringForChosenEventLocationService cateringForChosenEventLocationService

    CateringForLocationRepository cateringForLocationRepository
    OrganizedEventRepository organizedEventRepository
    CustomerRepository customerRepository
    CateringService cateringService
    TimestampHelper timestampHelper

    LocalDateTime now

    def setup() {
        cateringForLocationRepository = Mock()
        organizedEventRepository = Mock()
        customerRepository = Mock()
        cateringService = Mock()
        timestampHelper = Mock()

        now = LocalDateTime.parse('2007-12-03T10:15:30')
        timestampHelper.now() >> now

        cateringForChosenEventLocationService = new CateringForChosenEventLocationService(
                cateringForLocationRepository,
                organizedEventRepository,
                customerRepository,
                cateringService,
                timestampHelper
        )
    }

    def "ConfirmReservation"() {
        given:
        def cateringId = 1L
        def eventId = 2L
        def catering = fakeCateringForChosenEventLocation
        catering.setConfirmationStatus(CONFIRMED.name())

        def organizedEvent = catering.getEventLocation().getEvent()
        organizedEvent.setModifiedAt(now)

        def target = catering

        when:
        def result = cateringForChosenEventLocationService.confirmReservation(cateringId, eventId)

        then:
        1 * cateringForLocationRepository.findByCateringIdAndEventId(cateringId, eventId) >> Optional.of(catering)
        1 * cateringForLocationRepository.save(catering)
        1 * organizedEventRepository.save(organizedEvent)

        result == target
    }

    def "Create"() {
        given:
        def customerId = 1L
        def eventId = 2L
        def cateringId = 3L
        def dto = fakeCateringForChosenEventLocationDto

        def organizedEvent = fakeOrganizedEvent

        def cateringWithBusinnessHours = fakeCatering
        def businessHours = fakeCateringBusinessHours
        fakeCatering.setCateringBusinessHours(Set.of(businessHours))

        def catering = fakeCatering
        organizedEvent.getLocationForEvent().iterator().next().getLocation().getCaterings().add(catering)

        def locationForEvent = organizedEvent.getLocationForEvent().iterator().next()
        locationForEvent.setEvent(organizedEvent)

        def target = CateringForChosenLocationMapper.fromDto(dto)
        target.setDate(organizedEvent.getDate())
        target.setEventLocation(organizedEvent.getLocationForEvent().iterator().next())
        target.setCatering(catering)

        when:
        def result = cateringForChosenEventLocationService.create(customerId, eventId, cateringId, dto)

        then:
        1 * customerRepository.existsById(customerId) >> true
        1 * organizedEventRepository.getWithLocation(eventId) >> Optional.of(organizedEvent)
        1 * cateringService.getWithBusinessHours(cateringId) >> cateringWithBusinnessHours
        1 * cateringService.get(cateringId) >> catering
        1 * cateringForLocationRepository.save(target)

        result == target
    }

    def "Create NotFoundException"() {
        given:
        def customerId = 1L
        def eventId = 2L
        def cateringId = 3L
        def dto = fakeCateringForChosenEventLocationDto

        def organizedEvent = fakeOrganizedEvent
        organizedEvent.setLocationForEvent(ImmutableSet.of())

        when:
        cateringForChosenEventLocationService.create(customerId, eventId, cateringId, dto)

        then:
        1 * customerRepository.existsById(customerId) >> true
        1 * organizedEventRepository.getWithLocation(eventId) >> Optional.of(organizedEvent)

        thrown(NotFoundException)
    }

    def "Create NotFoundException No catering on date"() {
        given:
        def customerId = 1L
        def eventId = 2L
        def cateringId = 3L
        def dto = fakeCateringForChosenEventLocationDto

        def organizedEvent = fakeOrganizedEvent
        organizedEvent.setDate(LocalDate.parse('2007-12-04'))

        def cateringWithBusinnessHours = fakeCatering
        def businessHours = fakeCateringBusinessHours
        fakeCatering.setCateringBusinessHours(Set.of(businessHours))

        when:
        cateringForChosenEventLocationService.create(customerId, eventId, cateringId, dto)

        then:
        1 * customerRepository.existsById(customerId) >> true
        1 * organizedEventRepository.getWithLocation(eventId) >> Optional.of(organizedEvent)
        1 * cateringService.getWithBusinessHours(cateringId) >> cateringWithBusinnessHours

        thrown(NotFoundException)
    }

    def "Create NotFoundException Catering cannot deliver to chosen location"() {
        given:
        def customerId = 1L
        def eventId = 2L
        def cateringId = 3L
        def dto = fakeCateringForChosenEventLocationDto

        def organizedEvent = fakeOrganizedEvent

        def cateringWithBusinnessHours = fakeCatering
        def businessHours = fakeCateringBusinessHours
        fakeCatering.setCateringBusinessHours(Set.of(businessHours))

        def catering = fakeCatering

        when:
        cateringForChosenEventLocationService.create(customerId, eventId, cateringId, dto)

        then:
        1 * customerRepository.existsById(customerId) >> true
        1 * organizedEventRepository.getWithLocation(eventId) >> Optional.of(organizedEvent)
        1 * cateringService.getWithBusinessHours(cateringId) >> cateringWithBusinnessHours
        1 * cateringService.get(cateringId) >> catering

        thrown(NotFoundException)
    }


    def "CancelReservation"() {
        given:
        def cateringForEventId = 1L
        def cateringForLocation = fakeCateringForChosenEventLocation
        def event = cateringForLocation.getEventLocation().getEvent()

        cateringForLocation.setConfirmationStatus(CANCELLATION_PENDING.name())
        event.setModifiedAt(now)

        def target = cateringForLocation

        when:
        def result = cateringForChosenEventLocationService.cancelReservation(cateringForEventId)

        then:
        1 * cateringForLocationRepository.getWithCateringAndEvent(cateringForEventId) >> Optional.of(cateringForLocation)
        1 * organizedEventRepository.save(event)
        1 * cateringForLocationRepository.save(cateringForLocation)

        result == target
    }

    def "cancelReservation"() {
        given:
        def locationForEventId = 1L
        def cateringForChosenEventLocation = fakeCateringForChosenEventLocation
        cateringForChosenEventLocation.setConfirmationStatus(CANCELLATION_PENDING.name())

        def event = cateringForChosenEventLocation.getEventLocation().getEvent()
        event.setModifiedAt(now)

        def target = CateringForChosenEventLocation.builder().build()
        InvokerHelper.setProperties(target, cateringForChosenEventLocation.properties)
        target.setConfirmationStatus(CANCELLED.name())

        when:
        def result = cateringForChosenEventLocationService.cancelReservation(locationForEventId)

        then:
        1 * cateringForLocationRepository.getWithCateringAndEvent(locationForEventId) >> Optional.of(cateringForChosenEventLocation)
        1 * organizedEventRepository.save(event)

        result == target
    }

    def "ListAllByStatus"() {
        given:
        def cateringId = 1L
        def status = "SAMPLE STATUS"

        def target = [fakeCateringForChosenEventLocation]
        when:
        def result = cateringForChosenEventLocationService.listAllByStatus(cateringId, status)

        then:
        1 * cateringForLocationRepository.findAllByCateringIdAndStatus(cateringId, status) >> target

        result == target
    }

    def "Get"() {
        given:
        def cateringId = 1L

        def target = fakeCateringForChosenEventLocation
        when:
        def result = cateringForChosenEventLocationService.get(cateringId)

        then:
        1 * cateringForLocationRepository.findById(cateringId) >> Optional.of(target)

        result == target
    }
}

