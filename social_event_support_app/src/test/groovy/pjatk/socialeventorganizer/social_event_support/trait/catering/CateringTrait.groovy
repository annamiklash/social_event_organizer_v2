package pjatk.socialeventorganizer.social_event_support.trait.catering

import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto
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
            .address(buildAddressDto())
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
}
