package pjatk.socialeventorganizer.social_event_support.trait.image

import pjatk.socialeventorganizer.social_event_support.address.model.Address
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.image.model.LocationImage
import pjatk.socialeventorganizer.social_event_support.location.model.Location

trait LocationImageTrait {

    LocationImage fakeLocationImage = LocationImage.builder()
            .id(1L)
            .image("SAMPLE IMAGE".getBytes())
            .fileName("SAMPLE FILE NAME")
            .location(Location.builder().id(1L)
                    .caterings(new HashSet<Catering>())
                    .locationAddress(Address.builder()
                            .id(1)
                            .country('Poland')
                            .city('Warsaw')
                            .streetName('Piękna')
                            .streetNumber(1)
                            .zipCode('01-157')
                            .build())
                    .build())
            .build()

}