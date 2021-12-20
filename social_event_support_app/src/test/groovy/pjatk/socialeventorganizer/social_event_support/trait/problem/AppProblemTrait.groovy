package pjatk.socialeventorganizer.social_event_support.trait.problem

import pjatk.socialeventorganizer.social_event_support.appproblem.model.AppProblem
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblemDto

trait AppProblemTrait {

    AppProblem fakeAppProblem = AppProblem.builder()
            .concern('error_1')
            .description('test')
            .build()

    AppProblemDto fakeAppProblemDto = AppProblemDto.builder()
            .concern('error_1')
            .description('test')
            .build()

}