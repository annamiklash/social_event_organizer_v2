package pjatk.socialeventorganizer.social_event_support.trait.customer.guest

import com.google.common.collect.ImmutableSet
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto
import pjatk.socialeventorganizer.social_event_support.user.model.dto.UserDto

import java.time.LocalDateTime

trait GuestTrait {

    Guest fakeGuest = Guest.builder()
            .id(1l)
            .firstName("Geralt")
            .lastName("Rivijski")
            .email('email@email.com')
            .createdAt(LocalDateTime.parse('2007-12-03T10:15:30'))
            .modifiedAt(LocalDateTime.parse('2007-12-03T10:15:30'))
            .organizedEvents(ImmutableSet.of())
            .build()

    GuestDto fakeGuestDTO = GuestDto.builder()
            .id(1l)
            .firstName("Geralt")
            .lastName("Rivijski")
            .email('email@email.com')
            .createdAt('2007-12-03T10:15:30')
            .modifiedAt('2007-12-03T10:15:30')
            .customer(CustomerDto.builder()
                    .phoneNumber('123123123')
                    .firstName("Geralt")
                    .lastName("Rivijski")
                    .user(UserDto.builder()
                            .id(1)
                            .type('C' as char)
                            .email("test@email.com")
                            .build()
                    )
                    .build())
            .build()

}