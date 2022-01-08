package pjatk.socialeventorganizer.social_event_support.trait.event

import com.google.common.collect.ImmutableSet
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto
import pjatk.socialeventorganizer.social_event_support.event.model.EventType
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent
import pjatk.socialeventorganizer.social_event_support.location.model.Location
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation
import pjatk.socialeventorganizer.social_event_support.user.model.dto.UserDto
import spock.lang.Shared

import java.time.LocalDate
import java.time.LocalTime

import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.IN_PROGRESS

trait OrganizedEventTrait {

    @Shared
    OrganizedEvent fakeOrganizedEvent = OrganizedEvent.builder()
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
            .customer(Customer.builder()
                    .id(1L)
                    .firstName('Geralt')
                    .lastName('Rivijski')
                    .birthdate(LocalDate.parse('2007-12-03'))
                    .phoneNumber(new BigInteger("123123123"))
                    .email('test@email.com')
                    .guests(new HashSet<>())
                    .events(new HashSet<>())
                    .build())
            .locationForEvent(LocationForEvent.builder()
                    .id(1L)
                    .confirmationStatus("CONFIRMED")
                    .event(OrganizedEvent.builder()
                            .build())
                    .location(Location.builder().id(2L)
                            .caterings(new HashSet<Catering>())
                            .build())
                    .services(ImmutableSet.of(
                            OptionalServiceForChosenLocation.builder()
                                    .id(1L)
                                    .confirmationStatus("CONFIRMED")
                                    .build()
                    ))
                    .cateringsForEventLocation(ImmutableSet.of(
                            CateringForChosenEventLocation.builder()
                                    .id(1L)
                                    .confirmationStatus('CONFIRMED')
                                    .build()
                    ))
                    .build())
            .build()

    OrganizedEventDto fakeOrganizedEventDto = OrganizedEventDto.builder()
            .id(1)
            .name("SAMPLE NAME")
            .date('2007-12-03')
            .startTime("10:00")
            .endTime("12:00")
            .guestCount(10)
            .eventStatus(IN_PROGRESS.name())
            .eventType("Party")
            .customer(CustomerDto.builder()
                    .firstName('Geralt')
                    .lastName('Rivijski')
                    .birthdate('2007-12-03')
                    .phoneNumber("123123123")
                    .user(UserDto.builder()
                            .id(1)
                            .type('C' as char)
                            .email('email@email.com')
                            .build())
                    .build())
            .build()
}