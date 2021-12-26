package pjatk.socialeventorganizer.social_event_support.availability.optionalservice.service


import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository.OptionalServiceAvailabilityRepository
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService
import pjatk.socialeventorganizer.social_event_support.trait.availability.AvailabilityTrait
import pjatk.socialeventorganizer.social_event_support.trait.availability.optionalservice.OptionalServiceAvailabilityTrait
import pjatk.socialeventorganizer.social_event_support.trait.optional_service.OptionalServiceTrait
import spock.lang.Specification
import spock.lang.Subject

class OptionalServiceAvailabilityServiceTest extends Specification
        implements AvailabilityTrait,
                OptionalServiceTrait,
                OptionalServiceAvailabilityTrait {

    @Subject
    OptionalServiceAvailabilityService optionalServiceAvailabilityService

    OptionalServiceAvailabilityRepository optionalServiceAvailabilityRepository
    OptionalServiceService optionalServiceService

    def setup() {
        optionalServiceAvailabilityRepository = Mock()
        optionalServiceService = Mock()

        optionalServiceAvailabilityService = new OptionalServiceAvailabilityService(
                optionalServiceAvailabilityRepository,
                optionalServiceService
        )
    }
    //todo: implement
    def "Update"() {
    }

    def "FindAllByServiceIdAndDate"() {
        given:
        def id = 1L
        def date = "11.22.1964"

        def target = [fakeOptionalServiceAvailability]

        when:
        def result = optionalServiceAvailabilityService.findAllByServiceIdAndDate(id, date)

        then:
        1 * optionalServiceAvailabilityRepository.findAvailabilitiesByServiceIdAndDate(id, date) >> target

        result == target

    }

    def "Delete"() {
        given:
        def locationId = 1L
        def date = "11.22.63"
        def optionalServiceAvailability = fakeOptionalServiceAvailability

        when:
        optionalServiceAvailabilityService.delete(locationId, date)

        then:
        1 * optionalServiceAvailabilityRepository.findAvailabilitiesByServiceIdAndDate(locationId, date) >> [fakeOptionalServiceAvailability]
        1 * optionalServiceAvailabilityRepository.delete(optionalServiceAvailability)
    }
    //todo: implement
    def "UpdateToAvailable"() {

    }

    def "GetByDateAndTime"() {
        given:
        def date = "11.22.63"
        def timeFrom = "10:00"
        def timeTo = "15:00"
        def target = fakeOptionalServiceAvailability

        when:
        def result = optionalServiceAvailabilityService.getByDateAndTime(date, timeFrom, timeTo)

        then:
        1 * optionalServiceAvailabilityRepository.getByDateAndTime(date, timeFrom, timeTo) >> Optional.of(target)

        result == target

    }
}
