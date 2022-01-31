package pjatk.socialeventorganizer.social_event_support.trait.business

import com.google.common.collect.ImmutableSet
import pjatk.socialeventorganizer.social_event_support.address.model.Address
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto
import pjatk.socialeventorganizer.social_event_support.business.model.Business
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine
import pjatk.socialeventorganizer.social_event_support.location.model.Location
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService
import pjatk.socialeventorganizer.social_event_support.user.model.dto.UserDto

trait BusinessTrait {

    BusinessDto fakeVerifiedBusinessDto = BusinessDto.builder()
            .id(1)
            .firstName('Name')
            .lastName('Name')
            .businessName('Name')
            .verificationStatus('VERIFIED')
            .address(buildAddress())
            .phoneNumber("123123123")
            .user(UserDto.builder()
                    .id(1)
                    .type('B' as char)
                    .email('test@email.com')
                    .build())
            .build()

    BusinessDto fakeNotVerifiedBusinessDto = BusinessDto.builder()
            .id(1)
            .firstName('Name')
            .lastName('Name')
            .businessName('Name')
            .verificationStatus('NOT_VERIFIED')
            .address(buildAddress())
            .build()

    Business fakeVerifiedBusiness = Business.builder()
            .id(1)
            .firstName('Name')
            .lastName('Name')
            .businessName('Name')
            .verificationStatus('VERIFIED')
            .isActive(true)
            .phoneNumber(new BigInteger("123123123"))
            .services(Set.of(
                    OptionalService.builder()
                            .id(1)
                            .type("HOST")
                            .alias("ALIAS")
                            .firstName("GERALT")
                            .lastName("RIVIJSKI")
                            .description("WIEDZMIN")
                            .serviceCost(new BigDecimal("123"))
                            .email("Test@test.com")
                            .build()
            ))
            .caterings(Set.of(
                    Catering.builder()
                            .id(1)
                            .name('Name')
                            .email('catering@email.com')
                            .phoneNumber(new BigInteger('123456789'))
                            .description('description')
                            .cateringAddress(Address.builder()
                                    .id(1)
                                    .country('Poland')
                                    .city('Warsaw')
                                    .streetName('Piękna')
                                    .streetNumber(1)
                                    .zipCode('01-157')
                                    .build())
                            .cuisines(ImmutableSet.of(
                                    Cuisine.builder()
                                            .id(1)
                                            .name('Greek')
                                            .build()
                            ))
                            .build()
            ))
            .locations(Set.of(
                    Location.builder()
                            .id(1)
                            .name('Name')
                            .email('email@email.com')
                            .locationAddress(Address.builder()
                                    .id(1)
                                    .country('Poland')
                                    .city('Warsaw')
                                    .streetName('Piękna')
                                    .streetNumber(1)
                                    .zipCode('01-157')
                                    .build())
                            .build()
            ))
            .address(Address.builder()
                    .id(1)
                    .country('Poland')
                    .city('Warsaw')
                    .streetName('Piękna')
                    .streetNumber(1)
                    .zipCode('01-157')
                    .build())
            .build()

    Business fakeNotVerifiedBusiness = Business.builder()
            .id(1)
            .firstName('Name')
            .lastName('Name')
            .businessName('Name')
            .verificationStatus('NOT_VERIFIED')
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