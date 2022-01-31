package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service

import com.google.common.collect.ImmutableList
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringItemService
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringOrderChoiceDto
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

        def target = ImmutableList.of(fakeCateringOrderChoiceConfirmed)

        when:
        def result = cateringOrderChoiceService.getAll(cateringId)

        then:
        1 * cateringOrderChoiceRepository.getAll(cateringId) >> target

        result == target
    }

    def "GetAll with reservation id"() {
        given:
        def cateringId = 1L
        def reservationId = 1L

        def target = ImmutableList.of(fakeCateringOrderChoiceConfirmed)

        when:
        def result = cateringOrderChoiceService.getAll(cateringId, reservationId)

        then:
        1 * cateringOrderChoiceRepository.getAll(cateringId, reservationId) >> target

        result == target
    }

    def "Create"() {
        given:
        def dtos = [fakeCateringOrderChoiceDto] as CateringOrderChoiceDto[]
        def reservationId = 1L

        def catering = fakeCateringForChosenEventLocation
        def cateringItem = fakeCateringItem

        def savedCateringItem = fakeCateringOrderChoiceNoId;
        savedCateringItem.setEventLocationCatering(catering)
        savedCateringItem.setItem(cateringItem)

        def target = [savedCateringItem]

        when:
        def result = cateringOrderChoiceService.create(dtos, reservationId)

        then:
        1 * cateringForChosenEventLocationService.get(reservationId) >> catering
        1 * cateringItemService.get(dtos.iterator().next().getItemId()) >> cateringItem
        1 * cateringOrderChoiceRepository.saveAll(target) >> target

        result == target
    }

    def "Edit"() {
        given:
        def dto = fakeCateringOrderChoiceDto
        def orderChoiceId = 1L

        def target = fakeCateringOrderChoiceNotConfirmed
        target.setAmount(dto.getAmount())

        when:
        def result = cateringOrderChoiceService.edit(dto, orderChoiceId)

        then:
        1 * cateringOrderChoiceRepository.findWithDetail(orderChoiceId) >> Optional.of(target)
        1 * cateringOrderChoiceRepository.save(target)

        result == target
    }

    def "Delete"() {
        given:
        def id = 1L
        def orderChoice = fakeCateringOrderChoiceNotConfirmed

        when:
        cateringOrderChoiceService.delete(id)

        then:
        1 * cateringOrderChoiceRepository.findWithDetail(id) >> Optional.of(orderChoice)
        1 * cateringOrderChoiceRepository.delete(orderChoice)
    }
}
