package pjatk.socialeventorganizer.social_event_support.location.locationforevent.service

import pjatk.socialeventorganizer.social_event_support.availability.location.service.LocationAvailabilityService
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository
import pjatk.socialeventorganizer.social_event_support.event.repository.OrganizedEventRepository
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.repository.LocationForEventRepository
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class LocationForEventServiceTest extends Specification {

    @Subject
    LocationForEventService locationForEventService

    LocationForEventRepository locationForEventRepository
    OrganizedEventRepository organizedEventRepository
    CustomerRepository customerRepository
    LocationService locationService
    LocationAvailabilityService locationAvailabilityService
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {
        locationForEventRepository = Mock()
        organizedEventRepository = Mock()
        customerRepository = Mock()
        locationService = Mock()
        locationAvailabilityService = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        locationForEventService = new LocationForEventService(locationForEventRepository, organizedEventRepository,
                customerRepository, locationService, locationAvailabilityService, timestampHelper)
    }

    def "Create"() {


    }

    def "GetWithLocation"() {
    }

    def "CancelReservation"() {
    }

    def "GetWithLocationAndEvent"() {
    }

    def "ConfirmReservation"() {
    }

    def "ListAllByStatus"() {
    }

    def "Save"() {
    }

    def "FindByLocationIdAndEventId"() {
    }

    def "ListAllByStatusAndBusinessId"() {
    }

    def "FindByEventId"() {
    }
}
