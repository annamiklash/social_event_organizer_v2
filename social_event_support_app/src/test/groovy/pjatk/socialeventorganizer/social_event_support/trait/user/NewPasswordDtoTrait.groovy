package pjatk.socialeventorganizer.social_event_support.trait.user

import pjatk.socialeventorganizer.social_event_support.user.model.dto.NewPasswordDto

trait NewPasswordDtoTrait {

    NewPasswordDto fakeNewPasswordDto = NewPasswordDto.builder()
            .password('123Password!')
            .build()
}
