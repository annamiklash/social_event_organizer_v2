package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service

import com.google.common.collect.ImmutableList
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringItemService
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper.CateringOrderChoiceMapper
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.repository.CateringOrderChoiceRepository
import pjatk.socialeventorganizer.social_event_support.trait.catering.CateringItemTrait
import pjatk.socialeventorganizer.social_event_support.trait.cateringforchosenevent.CateringForChosenEventLocationTrait
import pjatk.socialeventorganizer.social_event_support.trait.cateringforchosenevent.CateringOrderChoiceTrait
import spock.lang.Specification
import spock.lang.Subject

class CateringOrderChoiceServiceTest extends Specification
        implements CateringOrderChoiceTrait,
                CateringItemTrait,
                CateringForChosenEventLocationTrait {

    @Subject
    CateringOrderChoiceService cateringOrderChoiceService

    CateringOrderChoiceRepository cateringOrderChoiceRepository
    CateringForChosenEventLocationService cateringForChosenEventLocationService
    CateringItemService cateringItemService

    def setup() {
        cateringOrderChoiceRepository = Mock()
        cateringForChosenEventLocationService = Mock()
        cateringItemService = Mock()

        cateringOrderChoiceService = new CateringOrderChoiceService(
                cateringOrderChoiceRepository,
                cateringForChosenEventLocationService,
                cateringItemService)
    }

    def "GetAll"() {
        given:
        def cateringId = 1L

        def target = ImmutableList.of(fakeCateringOrderChoice)

        when:
        def result = cateringOrderChoiceService.getAll(cateringId)

        then:
        1 * cateringOrderChoiceRepository.getAll(cateringId) >> target

        result == target
    }

    def "Create"() {
        given:
        def dto = fakeCateringOrderChoiceDto
        def itemId = 1L
        def cateringId = 2L

        def catering = fakeCateringForChosenEventLocation
        def cateringItem = fakeCateringItem

        def target = CateringOrderChoiceMapper.fromDto(dto);
        target.setEventLocationCatering(catering)
        target.setItem(cateringItem)

        when:
        def result = cateringOrderChoiceService.create(dto, itemId, cateringId)

        then:
        1 * cateringForChosenEventLocationService.get(cateringId) >> catering
        1 * cateringItemService.get(itemId) >> cateringItem
        1 * cateringOrderChoiceRepository.save(target) >> target

        result == target
    }

    def "Edit"() {
        given:
        def dto = fakeCateringOrderChoiceDto
        def orderChoiceId = 1L

        def target = fakeCateringOrderChoice
        target.setAmount(dto.getAmount())

        when:
        def result = cateringOrderChoiceService.edit(dto, orderChoiceId)

        then:
        1 * cateringOrderChoiceRepository.findWithDetail(orderChoiceId) >> Optional.of(target)
        1* cateringOrderChoiceRepository.save(target)

        result == target
    }

    def "Delete"() {
        given:
        def id = 1L
        def orderChoice = fakeCateringOrderChoice

        when:
        cateringOrderChoiceService.delete(id)

        then:
        1 * cateringOrderChoiceRepository.findById(id) >> Optional.of(orderChoice)
        1 * cateringOrderChoiceRepository.delete(orderChoice)
    }
}
