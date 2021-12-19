package pjatk.socialeventorganizer.social_event_support.trait.address

import pjatk.socialeventorganizer.social_event_support.address.model.Address
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto

trait AddressTrait {

    Address fakeAddress = Address.builder()
            .country('Poland')
            .city('Warsaw')
            .streetName('Piękna')
            .streetNumber(1)
            .zipCode('01-157')
            .build()

    AddressDto fakeAddressDto = AddressDto.builder()
            .id(1)
            .country('Poland')
            .city('Warsaw')
            .streetName('Piękna')
            .streetNumber(1)
            .zipCode('01-157')
            .build()

}