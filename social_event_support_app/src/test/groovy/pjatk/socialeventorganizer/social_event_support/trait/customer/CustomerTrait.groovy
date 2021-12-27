package pjatk.socialeventorganizer.social_event_support.trait.customer

import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.dto.CustomerAvatarDto
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto
import pjatk.socialeventorganizer.social_event_support.user.model.User
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.UserDto

import java.time.LocalDate

trait CustomerTrait {

    Customer fakeCustomer = Customer.builder()
            .id(1L)
            .firstName('Geralt')
            .lastName('Rivijski')
            .birthdate(LocalDate.parse('2007-12-03'))
            .phoneNumber(new BigInteger("123123123"))
            .guests(new HashSet<>())
            .events(new HashSet<>())
            .user(User.builder()
                    .id(1)
                    .type('C' as char)
                    .email('email@email.com')
                    .build())
            .build()

    CustomerDto fakeCustomerDTO = CustomerDto.builder()
            .firstName('Geralt')
            .lastName('Rivijski')
            .birthdate('2007-12-03')
            .phoneNumber("123123123")
            .guests(new HashSet<>())
            .events(new HashSet<>())
            .user(UserDto.builder()
                    .id(1)
                    .type('C' as char)
                    .email('email@email.com')
                    .build())
            .avatar(CustomerAvatarDto.builder().id(1L).build())
            .build()


}