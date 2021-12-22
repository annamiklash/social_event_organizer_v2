package pjatk.socialeventorganizer.social_event_support.trait.location

import pjatk.socialeventorganizer.social_event_support.location.model.Location

trait LocationTrait {

    Location fakeLocation = Location.builder()
            .id(1)
            .name('Name')
            .email('email@email.com')
            .build()


}