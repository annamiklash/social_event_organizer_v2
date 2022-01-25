package pjatk.socialeventorganizer.social_event_support.trait.cateringforchosenevent

import com.google.common.collect.ImmutableSet
import com.google.common.collect.Sets
import pjatk.socialeventorganizer.social_event_support.address.model.Address
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine
import pjatk.socialeventorganizer.social_event_support.event.model.EventType
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent
import pjatk.socialeventorganizer.social_event_support.location.model.Location
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation

import java.time.LocalDate
import java.time.LocalTime

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.CONFIRMED
import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.NOT_CONFIRMED
import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.IN_PROGRESS

trait CateringForChosenEventLocationTrait {

    CateringForChosenEventLocation fakeCateringForChosenEventLocation = CateringForChosenEventLocation.builder()
            .time(LocalTime.parse('10:15'))
            .comment("SAMPLE COMMENT")
            .confirmationStatus(NOT_CONFIRMED.name())
            .isCateringOrderConfirmed(true)
            .eventLocation(LocationForEvent.builder()
                    .id(1L)
                    .event(OrganizedEvent.builder()
                            .date(LocalDate.parse('2022-12-03'))
                            .build())
                    .build())
            .build()


    CateringForChosenEventLocation fakeFullCateringForChosenEventLocation = CateringForChosenEventLocation.builder()
            .id(1L)
            .time(LocalTime.parse('10:15'))
            .comment("SAMPLE COMMENT")
            .confirmationStatus(NOT_CONFIRMED.name())
            .isCateringOrderConfirmed(true)
            .eventLocation(LocationForEvent.builder()
                    .id(1L)
                    .event(OrganizedEvent.builder()
                            .id(1L)
                            .name("SAMPLE NAME")
                            .date(LocalDate.parse('2007-12-03'))
                            .startTime(LocalTime.parse("10:00:00"))
                            .endTime(LocalTime.parse("12:00:00"))
                            .guestCount(10)
                            .eventStatus(IN_PROGRESS.name())
                            .eventType(EventType.builder()
                                    .id(1L)
                                    .type("Party")
                                    .build())
                            .locationForEvent(Set.of(LocationForEvent.builder()
                                    .id(1L)
                                    .confirmationStatus("CONFIRMED")
                                    .timeFrom(LocalTime.parse("10:00:00"))
                                    .timeTo(LocalTime.parse("12:00:00"))
                                    .event(OrganizedEvent.builder()
                                            .date(LocalDate.parse('2007-12-03'))
                                            .startTime(LocalTime.parse("10:00:00"))
                                            .endTime(LocalTime.parse("12:00:00"))
                                            .guestCount(10)
                                            .build())
                                    .location(Location.builder().id(2L)
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
                                    .services(ImmutableSet.of(OptionalServiceForChosenLocation.builder()
                                            .id(1L)
                                            .confirmationStatus("CONFIRMED")
                                            .build()))
                                    .cateringsForEventLocation(ImmutableSet.of(CateringForChosenEventLocation.builder()
                                            .id(1L)
                                            .time(LocalTime.parse('10:15'))
                                            .comment("SAMPLE COMMENT")
                                            .confirmationStatus(NOT_CONFIRMED.name())
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
                                                    .cuisines(Sets.newHashSet(Cuisine.builder()
                                                            .id(1)
                                                            .name('Greek')
                                                            .build()))
                                                    .build())
                                            .build()))
                                    .build()))
                            .build())
                    .build())
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
                    .cuisines(Sets.newHashSet(Cuisine.builder()
                            .id(1)
                            .name('Greek')
                            .build()))
                    .build())

            .build()

    CateringForChosenEventLocationDto fakeCateringForChosenEventLocationDto = CateringForChosenEventLocationDto.builder()
            .id(1L)
            .time('10:15')
            .comment("SAMPLE COMMENT")
            .isOrderConfirmed(false)
            .confirmationStatus(CONFIRMED.name())
            .build()
}