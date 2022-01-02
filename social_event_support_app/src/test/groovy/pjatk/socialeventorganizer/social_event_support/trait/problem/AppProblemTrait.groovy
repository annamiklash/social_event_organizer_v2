package pjatk.socialeventorganizer.social_event_support.trait.problem

import pjatk.socialeventorganizer.social_event_support.appproblem.model.AppProblem
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblemDto
import pjatk.socialeventorganizer.social_event_support.user.model.User

trait AppProblemTrait {

    AppProblem fakeAppProblem = AppProblem.builder()
            .id(1L)
            .concern('error_1')
            .description('test')
            .build()

    AppProblem fakeAppProblemWithUser = AppProblem.builder()
            .id(1L)
            .concern('error_1')
            .description('test')
            .user(User.builder()
                    .id(1)
                    .type('C' as char)
                    .email('test@email.com')
                    .build())
            .build()

    AppProblemDto fakeAppProblemDto = AppProblemDto.builder()
            .id(1)
            .concern('error_1')
            .description('test')
            .build()

}