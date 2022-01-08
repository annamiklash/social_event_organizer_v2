package pjatk.socialeventorganizer.social_event_support.trait.cateringforchosenevent

import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringOrderChoice
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringOrderChoiceDto

trait CateringOrderChoiceTrait {

    CateringOrderChoice fakeCateringOrderChoice = CateringOrderChoice.builder()
            .id(1L)
            .amount(10)
            .item(CateringItem.builder()
                    .name('Name')
                    .itemType('Appetizer')
                    .description('SAMPLE DESCRIPTION')
                    .isVegan(true)
                    .isVegetarian(true)
                    .isGlutenFree(true)
                    .servingPrice(new BigDecimal('123456.00'))
                    .build())
            .build()

    CateringOrderChoiceDto fakeCateringOrderChoiceDto = CateringOrderChoiceDto.builder()
            .id(1L)
            .amount(10)
            .build()
}