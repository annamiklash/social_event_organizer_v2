package pjatk.socialeventorganizer.social_event_support.trait.location

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import pjatk.socialeventorganizer.social_event_support.address.model.Address
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto
import pjatk.socialeventorganizer.social_event_support.availability.location.model.LocationAvailability
import pjatk.socialeventorganizer.social_event_support.business.model.Business
import pjatk.socialeventorganizer.social_event_support.businesshours.location.model.LocationBusinessHours
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.location.model.Location
import pjatk.socialeventorganizer.social_event_support.location.model.LocationDescriptionItem
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto

trait LocationTrait {

    Location fakeLocation = Location.builder()
            .id(1)
            .name('Name')
            .email('email@email.com')
            .build()

    Location fakeFullLocation = Location.builder()
            .id(1L)
            .caterings(new HashSet<Catering>())
            .descriptions(new HashSet<LocationDescriptionItem>())
            .availability(new HashSet<LocationAvailability>())
            .locationBusinessHours(new HashSet<LocationBusinessHours>())
            .locationAddress(Address.builder()
                    .id(1)
                    .country('Poland')
                    .city('Warsaw')
                    .streetName('Piękna')
                    .streetNumber(1)
                    .zipCode('01-157')
                    .build())
            .business(Business.builder()
                    .id(1)
                    .firstName('Name')
                    .lastName('Name')
                    .businessName('Name')
                    .verificationStatus('VERIFIED')
                    .phoneNumber(new BigInteger("123123123"))
                    .build())
            .build()

    LocationDto fakeLocationDto = LocationDto.builder()
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
            .build()


}