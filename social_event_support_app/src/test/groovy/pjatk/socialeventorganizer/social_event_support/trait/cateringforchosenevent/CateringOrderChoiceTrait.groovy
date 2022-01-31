package pjatk.socialeventorganizer.social_event_support.trait.cateringforchosenevent

import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringOrderChoice
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringOrderChoiceDto

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.CONFIRMED

trait CateringOrderChoiceTrait {

    CateringOrderChoice fakeCateringOrderChoiceConfirmed = CateringOrderChoice.builder()
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
            .eventLocationCatering(CateringForChosenEventLocation.builder()
                    .id(1L)
                    .isCateringOrderConfirmed(true)
                    .confirmationStatus(CONFIRMED.name())
                    .build())
            .build()

    CateringOrderChoice fakeCateringOrderChoiceNotConfirmed = CateringOrderChoice.builder()
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
            .eventLocationCatering(CateringForChosenEventLocation.builder()
                    .id(1L)
                    .isCateringOrderConfirmed(false)
                    .confirmationStatus(CONFIRMED.name())
                    .build())
            .build()

    CateringOrderChoiceDto fakeCateringOrderChoiceDto = CateringOrderChoiceDto.builder()
            .id(1L)
            .itemId(1l)
            .amount(10)
            .build()

    CateringOrderChoice fakeCateringOrderChoiceNoId = CateringOrderChoice
            .builder()
            .amount(10)
            .build()
}