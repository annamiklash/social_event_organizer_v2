package pjatk.socialeventorganizer.social_event_support.trait.user

import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto
import pjatk.socialeventorganizer.social_event_support.user.model.dto.BusinessUserRegistrationDto
import pjatk.socialeventorganizer.social_event_support.user.model.dto.UserDto

trait BusinessUserRegistrationDtoTrait {

    BusinessUserRegistrationDto fakeBusinessUserRegistrationDto = BusinessUserRegistrationDto.builder()
            .email('test@email.com')
            .password('123Password!')
            .firstName('Geralt')
            .lastName('Rivijski')
            .phoneNumber('123123123')
            .businessName('Wiedzmin')
            .address(AddressDto.builder()
                    .id(1L)
                    .country('Poland')
                    .city('Warsaw')
                    .streetName('Piękna')
                    .streetNumber(1)
                    .zipCode('01-157')
                    .build())
            .user(UserDto.builder()
                    .id(1)
                    .type('C' as char)
                    .email('test@email.com')
                    .build())
            .verificationStatus('VERIFIED')
            .build()
}