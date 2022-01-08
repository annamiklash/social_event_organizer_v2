package pjatk.socialeventorganizer.social_event_support.trait.user

import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.dto.CustomerAvatarDto
import pjatk.socialeventorganizer.social_event_support.user.model.dto.CustomerUserRegistrationDto
import pjatk.socialeventorganizer.social_event_support.user.model.dto.UserDto

trait CustomerUserRegistrationDtoTrait {

    CustomerUserRegistrationDto fakeCustomerUserRegistrationDto = CustomerUserRegistrationDto.builder()
            .email('test@email.com')
            .password('123Password!')
            .firstName('Geralt')
            .lastName('Rivijski')
            .birthdate('2007-12-03')
            .phoneNumber('123123123')
            .user(UserDto.builder()
                    .id(1)
                    .type('C' as char)
                    .email('test@email.com')
                    .build())
            .avatar(CustomerAvatarDto.builder().id(1L).build())
            .build()

}