package pjatk.socialeventorganizer.social_event_support.trait.user

import pjatk.socialeventorganizer.social_event_support.user.model.User
import pjatk.socialeventorganizer.social_event_support.user.model.dto.UserDto

trait UserTrait {

    User fakeUser = User.builder()
            .id(1)
            .type('C' as char)
            .email('test@email.com')
            .build()

    UserDto fakeUserDto = UserDto.builder()
            .id(1)
            .type('C' as char)
            .email('test@email.com')
            .build()
}
