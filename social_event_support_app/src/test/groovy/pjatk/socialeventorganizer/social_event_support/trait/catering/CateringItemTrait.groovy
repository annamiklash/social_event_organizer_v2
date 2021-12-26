package pjatk.socialeventorganizer.social_event_support.trait.catering

import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringItemDto

trait CateringItemTrait {

    CateringItem fakeCateringItem = CateringItem.builder()
            .name('Name')
            .build()

    CateringItem fakeCateringItemWithId = CateringItem.builder()
            .id(1)
            .name('Name')
            .build()

    CateringItemDto fakeCateringItemDto = CateringItemDto.builder()
            .name('Name')
            .build()

}