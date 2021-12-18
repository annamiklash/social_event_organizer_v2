package pjatk.socialeventorganizer.social_event_support.trait.business


import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto
import pjatk.socialeventorganizer.social_event_support.business.model.Business
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto

trait BusinessTrait {

    BusinessDto fakeVerifiedBusinessDto = BusinessDto.builder()
            .id(1)
            .businessName('Name')
            .verificationStatus('VERIFIED')
            .address(buildAddress())
            .build();

    BusinessDto fakeNotVerifiedBusinessDto = BusinessDto.builder()
            .id(1)
            .businessName('Name')
            .verificationStatus('NOT_VERIFIED')
            .address(buildAddress())
            .build();

    Business fakeVerifiedBusiness = Business.builder()
            .id(1)
            .businessName('Name')
            .verificationStatus('VERIFIED')
            .build()

    Business fakeNotVerifiedBusiness = Business.builder()
            .id(1)
            .businessName('Name')
            .verificationStatus('VERIFIED')
            .build()

    def buildAddress() {
        return AddressDto.builder()
                .country('Poland')
                .city('Warsaw')
                .streetName('Piękna')
                .streetNumber(1)
                .build()
    }

}