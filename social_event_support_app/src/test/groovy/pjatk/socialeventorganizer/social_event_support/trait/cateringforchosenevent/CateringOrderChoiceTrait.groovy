package pjatk.socialeventorganizer.social_event_support.trait.cateringforchosenevent

import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringOrderChoice
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringOrderChoiceDto

trait CateringOrderChoiceTrait {

    CateringOrderChoice fakeCateringOrderChoice = CateringOrderChoice.builder()
            .id(1L)
            .amount(10)
            .build()

    CateringOrderChoiceDto fakeCateringOrderChoiceDto = CateringOrderChoiceDto.builder()
            .id(1L)
            .amount(10)
            .build()
}