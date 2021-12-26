package pjatk.socialeventorganizer.social_event_support.trait.customer.guest

import com.google.common.collect.ImmutableSet
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto

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
            .build()

}