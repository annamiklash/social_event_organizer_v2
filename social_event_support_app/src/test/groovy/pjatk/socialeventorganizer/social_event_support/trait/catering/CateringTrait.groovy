package pjatk.socialeventorganizer.social_event_support.trait.catering

import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto
import pjatk.socialeventorganizer.social_event_support.businesshours.DayEnum
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto
import pjatk.socialeventorganizer.social_event_support.cuisine.model.dto.CuisineDto

trait CateringTrait {

    CateringDto fakeCateringDtoOffersOutsideCatering = CateringDto.builder()
            .name('Name')
            .email('catering@email.com')
            .phoneNumber('123456789')
            .serviceCost('100.20')
            .description('description')
            .offersOutsideCatering(true)
            .cuisines(List.of(buildCuisine()))
            .businessHours(List.of(buildBusinessHoursDto()))
            .address(buildAddressDto())
            .build()

    Catering fakeCateringOffersOutsideCatering = Catering.builder()
            .name('Name')
            .email('catering@email.com')
            .phoneNumber(new BigInteger('123456789'))
            .description('description')
            .build()

    def buildAddressDto() {
        return AddressDto.builder()
                .id(1)
                .country('Poland')
                .city('Warsaw')
                .streetName('Piękna')
                .streetNumber(1)
                .build()
    }

    def buildCuisine() {
        return CuisineDto.builder()
                .name('Greek')
                .build()
    }

    def buildBusinessHoursDto() {
        return BusinessHoursDto.builder()
                .id(1)
                .day(DayEnum.MONDAY)
                .timeFrom('10')
                .timeTo('20')
                .build()
    }
}
