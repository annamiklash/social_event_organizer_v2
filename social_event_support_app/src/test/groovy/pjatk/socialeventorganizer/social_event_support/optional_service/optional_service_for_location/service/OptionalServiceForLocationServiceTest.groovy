package pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.service

import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository.OptionalServiceAvailabilityRepository
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.service.OptionalServiceAvailabilityService
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository
import pjatk.socialeventorganizer.social_event_support.event.repository.OrganizedEventRepository
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.repostory.OptionalServiceForChosenLocationRepository
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService
import pjatk.socialeventorganizer.social_event_support.trait.availability.AvailabilityTrait
import pjatk.socialeventorganizer.social_event_support.trait.customer.CustomerTrait
import pjatk.socialeventorganizer.social_event_support.trait.event.OrganizedEventTrait
import pjatk.socialeventorganizer.social_event_support.trait.location.locationforevent.LocationForEventTrait
import pjatk.socialeventorganizer.social_event_support.trait.optional_service.OptionalServiceForChosenLocationTrait
import pjatk.socialeventorganizer.social_event_support.trait.optional_service.OptionalServiceTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.Month

class OptionalServiceForLocationServiceTest extends Specification implements OptionalServiceForChosenLocationTrait,
        CustomerTrait, OrganizedEventTrait, OptionalServiceTrait, AvailabilityTrait,
        LocationForEventTrait {

    @Subject
    OptionalServiceForLocationService optionalServiceForLocationService

    OptionalServiceForChosenLocationRepository optionalServiceForChosenLocationRepository
    CustomerRepository customerRepository
    OrganizedEventRepository organizedEventRepository
    OptionalServiceService optionalServiceService
    OptionalServiceAvailabilityRepository optionalServiceAvailabilityRepository
    OptionalServiceAvailabilityService optionalServiceAvailabilityService

    def setup() {
        optionalServiceForChosenLocationRepository = Mock()
        customerRepository = Mock()
        organizedEventRepository = Mock()
        optionalServiceService = Mock()
        optionalServiceAvailabilityRepository = Mock()
        optionalServiceAvailabilityService = Mock()

        optionalServiceForLocationService = new OptionalServiceForLocationService(optionalServiceForChosenLocationRepository,
                customerRepository,
                organizedEventRepository,
                optionalServiceService,
                optionalServiceAvailabilityRepository,
                optionalServiceAvailabilityService)
    }

    def "Create"() {
        given:
        def customerId = 1l
        def eventId = 1l
        def serviceId = 1l
        def dto = fakeOptionalServiceForChosenLocationDtoBasic
        def customer = fakeCustomer
        def event = fakeFullOrganizedEvent
        event.setDate(LocalDate.of(2022, Month.FEBRUARY, 1))
        def date = DateTimeUtil.fromLocalDateToDateString(event.getDate())
        def service = fakeOptionalHostWithAvailability
        def availability = fakeAvailabilityDto
        def availabilities = [fakeAvailabilityDto]
        def locationReservation = fakeLocationForEvent
        def serviceReservation = fakeOptionalServiceForChosenLocationSimpleNoId
        serviceReservation.setOptionalService(service)
        serviceReservation.setLocationForEvent(locationReservation)

        def target = serviceReservation
        when:
        def result = optionalServiceForLocationService.create(customerId, eventId, serviceId, dto)

        then:
        1 * customerRepository.findById(customerId) >> Optional.of(customer)
        1 * organizedEventRepository.getWithLocation(eventId) >> Optional.of(event)
        1 * optionalServiceService.isAvailable(serviceId, date, dto.getTimeFrom(), dto.getTimeTo())
        1 * optionalServiceService.get(serviceId) >> service
        1 * optionalServiceAvailabilityRepository.delete(availability)
        1 * optionalServiceAvailabilityRepository.saveAndFlush(availabilities.iterator().next())
        1 * optionalServiceForChosenLocationRepository.save(serviceReservation)

        result == target
    }

    def "ModifyAvailabilityAfterBooking"() {
    }

    def "ConfirmReservation"() {
    }

    def "ListAllByStatus"() {
    }

    def "CancelReservation"() {
    }

    def "GetWithServiceAndEvent"() {
    }

    def "ListAllByStatusAndBusinessId"() {
    }
}
