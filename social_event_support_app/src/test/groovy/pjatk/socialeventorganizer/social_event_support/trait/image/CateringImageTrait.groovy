package pjatk.socialeventorganizer.social_event_support.trait.image

import pjatk.socialeventorganizer.social_event_support.address.model.Address
import pjatk.socialeventorganizer.social_event_support.businesshours.DayEnum
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine
import pjatk.socialeventorganizer.social_event_support.image.model.CateringImage
import pjatk.socialeventorganizer.social_event_support.location.model.Location

import java.time.LocalTime

trait CateringImageTrait {

    CateringImage fakeCateringImage = CateringImage.builder()
            .id(1L)
            .image("SAMPLE IMAGE".getBytes())
            .fileName("SAMPLE FILE NAME")
            .catering(Catering.builder()
                    .id(1L)
                    .name('Name')
                    .email('email@email.com')
                    .phoneNumber(new BigInteger('123456789'))
                    .description('description')
                    .cateringAddress(Address.builder()
                            .id(1L)
                            .country('Poland')
                            .city('Warsaw')
                            .streetName('Piękna')
                            .streetNumber(1)
                            .zipCode('01-157')
                            .build())
                    .cuisines(Set.of(
                            Cuisine.builder()
                                    .id(1)
                                    .name('Greek')
                                    .build()
                    ))
                    .cateringItems(Set.of(
                            CateringItem.builder()
                                    .id(1)
                                    .name('Name')
                                    .build()
                    ))
                    .cateringBusinessHours(Set.of(
                            CateringBusinessHours.builder()
                                    .day(DayEnum.MONDAY.name())
                                    .timeFrom(LocalTime.of(10, 0))
                                    .timeTo(LocalTime.of(20, 0))
                                    .build()
                    ))
                    .locations(Set.of(
                            Location.builder()
                                    .id(1)
                                    .name('Name')
                                    .email('email@email.com')
                                    .locationAddress(Address.builder()
                                            .id(1)
                                            .country('Poland')
                                            .city('Warsaw')
                                            .streetName('Piękna')
                                            .streetNumber(1)
                                            .zipCode('01-157')
                                            .build())
                                    .build()
                    ))
                    .build())
            .build()

}