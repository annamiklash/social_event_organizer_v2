package pjatk.socialeventorganizer.social_event_support.businesshours.location.service

import pjatk.socialeventorganizer.social_event_support.businesshours.location.repository.LocationBusinessHoursRepository
import pjatk.socialeventorganizer.social_event_support.trait.BusinessHoursTrait
import spock.lang.Specification
import spock.lang.Subject

class LocationBusinessHoursServiceTest extends Specification implements BusinessHoursTrait {

    @Subject
    LocationBusinessHoursService locationBusinessHoursService

    LocationBusinessHoursRepository locationBusinessHoursRepository

    def setup() {
        locationBusinessHoursRepository = Mock()

        locationBusinessHoursService = new LocationBusinessHoursService(locationBusinessHoursRepository)
    }

    def "create() positive scenario"() {
        given:
        def locationBusinessHoursDto = fakeBusinessHoursDto
        def locationBusinessHoursDtoList = [locationBusinessHoursDto]
        def locationBusinessHours = fakeLocationBusinessHours
        def target = [fakeLocationBusinessHours]

        when:
        def result = locationBusinessHoursService.create(locationBusinessHoursDtoList)

        then:
        1 * locationBusinessHoursRepository.save(locationBusinessHours)

        result == target
    }

    def "delete() positive scenario"() {
        given:
        def locationBusinessHours = fakeLocationBusinessHours

        when:
        locationBusinessHoursService.delete(locationBusinessHours)

        then:
        1 * locationBusinessHoursRepository.delete(locationBusinessHours)
    }

    def "edit() positive scenario"() {
        given:
        def id = 1
        def locationBusinessHoursDto = fakeBusinessHoursDto
        def locationBusinessHours = fakeLocationBusinessHoursWithId
        def target = locationBusinessHours

        when:
        def result = locationBusinessHoursService.edit(id, locationBusinessHoursDto)

        then:
        1 * locationBusinessHoursRepository.findById(id) >> Optional.of(locationBusinessHours)
        1 * locationBusinessHoursRepository.save(locationBusinessHours)

        result == target
    }

}
