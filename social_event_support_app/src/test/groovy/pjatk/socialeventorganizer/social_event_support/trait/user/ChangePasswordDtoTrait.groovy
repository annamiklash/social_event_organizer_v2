package pjatk.socialeventorganizer.social_event_support.trait.user

import pjatk.socialeventorganizer.social_event_support.user.model.dto.ChangePasswordDto

trait ChangePasswordDtoTrait {

    ChangePasswordDto fakeChangePasswordDto = ChangePasswordDto.builder()
            .oldPassword('123Password!')
            .newPassword('123Password@')
            .build()

}