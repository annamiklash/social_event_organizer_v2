package pjatk.socialeventorganizer.social_event_support.trait.optional_service

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.google.common.collect.Sets
import pjatk.socialeventorganizer.social_event_support.address.model.Address
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto.LocationForEventDto
import pjatk.socialeventorganizer.social_event_support.location.model.Location
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.dto.OptionalServiceForChosenLocationDto
import pjatk.socialeventorganizer.social_event_support.user.model.dto.UserDto

import java.time.LocalDate
import java.time.LocalTime

import static pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum.NOT_CONFIRMED
import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.IN_PROGRESS

trait OptionalServiceForChosenLocationTrait {

    OptionalServiceForChosenLocation fakeOptionalServiceForChosenLocation = OptionalServiceForChosenLocation.builder()
            .id(1L)
            .timeFrom(LocalTime.parse("10:00:00"))
            .timeTo(LocalTime.parse("12:00:00"))
            .comment("SAMPLE COMMENT")
            .confirmationStatus("CONFIRMED")
            .locationForEvent(LocationForEvent.builder()
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
                    .build())
            .optionalService(OptionalService.builder()
                    .id(1)
                    .type("HOST")
                    .alias("ALIAS")
                    .firstName("GERALT")
                    .lastName("RIVIJSKI")
                    .description("WIEDZMIN")
                    .serviceCost(new BigDecimal("123"))
                    .email("Test@test.com")
                    .build())
            .build()

    OptionalServiceForChosenLocation fakeOptionalServiceForChosenLocationSimpleNoId = OptionalServiceForChosenLocation.builder()
            .timeFrom(LocalTime.parse("10:00:00"))
            .timeTo(LocalTime.parse("12:00:00"))
            .comment("SAMPLE COMMENT")
            .confirmationStatus("CONFIRMED")
            .build()

    OptionalServiceForChosenLocationDto fakeOptionalServiceForChosenLocationDtoBasic = OptionalServiceForChosenLocationDto.builder()
            .timeFrom('13:00')
            .timeTo('18:00')
            .comment('SAMPLE COMMENT')
            .build()

    OptionalServiceForChosenLocationDto fakeOptionalServiceForChosenLocationDto = OptionalServiceForChosenLocationDto.builder()
            .timeFrom('10:00')
            .timeTo('12:00')
            .comment('SAMPLE COMMENT')
            .confirmationStatus('CONFIRMED')
            .optionalService(OptionalServiceDto.builder()
                    .id(1)
                    .type("HOST")
                    .alias("ALIAS")
                    .firstName("GERALT")
                    .lastName("RIVIJSKI")
                    .description("WIEDZMIN")
                    .serviceCost("123")
                    .email('email@email.com')
                    .businessHours(new ArrayList<BusinessHoursDto>())
                    .address(AddressDto.builder().build())
                    .build())
            .locationForEvent(LocationForEventDto.builder()
                    .id(1L)
                    .timeFrom('10:00')
                    .timeTo('12:00')
                    .guestCount(1)
                    .date('2012-01-01')
                    .confirmationStatus('CONFIRMED')
                    .caterings(new ArrayList<CateringForChosenEventLocationDto>())
                    .optionalServices(new ArrayList<OptionalServiceForChosenLocationDto>())
                    .location(LocationDto.builder()
                            .id(1L)
                            .name("SAMPLE LOCATION NAME")
                            .rating(12.10D)
                            .email("test@email.com")
                            .phoneNumber('123123123')
                            .seatingCapacity(10)
                            .standingCapacity(20)
                            .description("SAMPLE DESCRIPTION")
                            .dailyRentCost("123")
                            .sizeInSqMeters(100)
                            .descriptions(ImmutableSet.of())
                            .address(AddressDto.builder()
                                    .id(1L)
                                    .country('Poland')
                                    .city('Warsaw')
                                    .streetName('Piękna')
                                    .streetNumber(1)
                                    .zipCode('01-157')
                                    .build())
                            .businessHours(ImmutableList.of())
                            .build())
                    .event(OrganizedEventDto.builder()
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
                            .build())
                    .build())
            .build();

}