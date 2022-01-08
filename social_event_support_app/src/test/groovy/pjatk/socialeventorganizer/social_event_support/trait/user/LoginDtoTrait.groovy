package pjatk.socialeventorganizer.social_event_support.trait.user

import pjatk.socialeventorganizer.social_event_support.user.model.dto.LoginDto

trait LoginDtoTrait {

    LoginDto fakeLoginDto = LoginDto.builder()
            .email('test@email.com')
            .password('123Password!')
            .build()

}