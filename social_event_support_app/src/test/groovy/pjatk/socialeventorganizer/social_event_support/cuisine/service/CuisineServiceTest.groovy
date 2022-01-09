package pjatk.socialeventorganizer.social_event_support.cuisine.service

import pjatk.socialeventorganizer.social_event_support.cuisine.mapper.CuisineMapper
import pjatk.socialeventorganizer.social_event_support.cuisine.repository.CuisineRepository
import pjatk.socialeventorganizer.social_event_support.trait.catering.CuisineTrait
import spock.lang.Specification
import spock.lang.Subject

class CuisineServiceTest extends Specification
        implements CuisineTrait {

    @Subject
    CuisineService cuisineService

    CuisineRepository cuisineRepository

    def setup() {
        cuisineRepository = Mock()

        cuisineService = new CuisineService(cuisineRepository)
    }

    def "List"() {
        given:
        def target = [fakeCuisine]

        when:
        def result = cuisineService.list()

        then:
        1 * cuisineRepository.findAll() >> target

        result == target
    }

    def "Create"() {
        given:
        def dto = fakeCuisineDto
        def name = dto.getName()

        def target = CuisineMapper.fromDto(dto)

        when:
        def result = cuisineService.create(dto)

        then:
        1 * cuisineRepository.existsByName(name) >> false
        1 * cuisineRepository.save(target)

        result == target
    }

    def "GetByName"() {
        given:
        def name = "abc"
        def target = fakeCuisine

        when:
        def result = cuisineService.getByName(name)

        then:
        1 * cuisineRepository.findByName(name) >> target

        result == target
    }
}
