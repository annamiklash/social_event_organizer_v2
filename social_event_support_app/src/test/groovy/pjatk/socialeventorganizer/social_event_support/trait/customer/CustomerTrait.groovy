package pjatk.socialeventorganizer.social_event_support.trait.customer


import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.CustomerAvatar
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto
import pjatk.socialeventorganizer.social_event_support.user.model.dto.UserDto

import java.time.LocalDate

trait CustomerTrait {

    Customer fakeCustomer = Customer.builder()
            .id(1L)
            .firstName('Geralt')
            .lastName('Rivijski')
            .birthdate(LocalDate.parse('2007-12-03'))
            .phoneNumber(new BigInteger("123123123"))
            .email('email@email.com')
            .guests(new HashSet<>())
            .events(new HashSet<>())
            .appProblems(new HashSet<>())
            .avatar(CustomerAvatar.builder().id(1L).image("image".getBytes()).fileName("name").build())
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
            .avatar(ImageDto.builder().id(1L).build())
            .build()


}