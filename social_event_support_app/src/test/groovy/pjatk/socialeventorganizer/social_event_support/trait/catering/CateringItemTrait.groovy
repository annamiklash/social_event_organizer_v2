package pjatk.socialeventorganizer.social_event_support.trait.catering

import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringItemDto

trait CateringItemTrait {

    CateringItem fakeCateringItem = CateringItem.builder()
            .name('Name')
            .itemType('Appetizer')
            .description('SAMPLE DESCRIPTION')
            .isVegan(true)
            .isVegetarian(true)
            .isGlutenFree(true)
            .servingPrice(new BigDecimal('123456.00'))
            .build()

    CateringItem fakeCateringItemWithId = CateringItem.builder()
            .id(1)
            .name('Name')
            .build()

    CateringItemDto fakeCateringItemDto = CateringItemDto.builder()
            .name('Name')
            .type('Appetizer')
            .description('SAMPLE DESCRIPTION')
            .isVegan(true)
            .isVegetarian(true)
            .isGlutenFree(true)
            .servingPrice('123456.00')
            .build()

}