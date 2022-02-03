package pjatk.socialeventorganizer.social_event_support.trait.catering

import com.google.common.collect.Sets
import pjatk.socialeventorganizer.social_event_support.address.model.Address
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto
import pjatk.socialeventorganizer.social_event_support.business.model.Business
import pjatk.socialeventorganizer.social_event_support.businesshours.DayEnum
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine
import pjatk.socialeventorganizer.social_event_support.cuisine.model.dto.CuisineDto
import pjatk.socialeventorganizer.social_event_support.image.model.CateringImage
import pjatk.socialeventorganizer.social_event_support.location.model.Location

import java.time.LocalTime

trait CateringTrait {

    CateringDto fakeCateringDtoOffersOutsideCatering = CateringDto.builder()
            .name('Name')
            .email('email@email.com')
            .phoneNumber('123456789')
            .serviceCost('100.20')
            .description('description')
            .offersOutsideCatering(true)
            .cuisines(List.of(
                    CuisineDto.builder()
                            .name('Greek')
                            .build()
            ))
            .businessHours(List.of(buildBusinessHoursDto()))
            .address(buildAddressDto())
            .build()

    Catering fakeCateringOffersOutsideCatering = Catering.builder()
            .name('Name')
            .email('email@email.com')
            .phoneNumber(new BigInteger('123456789'))
            .description('description')
            .build()

    Catering fakeCatering = Catering.builder()
            .id(1L)
            .name('Name')
            .email('email@email.com')
            .phoneNumber(new BigInteger('123456789'))
            .description('description')
            .offersOutsideCatering(true)
            .cateringAddress(Address.builder()
                    .id(1L)
                    .country('Poland')
                    .city('Warsaw')
                    .streetName('Piękna')
                    .streetNumber(1)
                    .zipCode('01-157')
                    .build())
            .cuisines(Sets.newHashSet(
                    Cuisine.builder()
                            .id(1)
                            .name('Greek')
                            .build()
            ))
            .images(Set.of(CateringImage.builder()
                    .id(1l)
                    .fileName("fileName")
                    .image("file.getBytes()".getBytes())
                    .build()))
            .build()

    Catering fakeCateringWithDetails = Catering.builder()
            .id(1L)
            .name('Name')
            .email('email@email.com')
            .phoneNumber(new BigInteger('123456789'))
            .description('description')
            .serviceCost(new BigDecimal(100.00))
            .business(Business.builder()
                    .id(1)
                    .firstName('Name')
                    .lastName('Name')
                    .businessName('Name')
                    .email('business@email.com')
                    .verificationStatus('VERIFIED')
                    .phoneNumber(new BigInteger("123123123"))
                    .isActive(true)
                    .build())
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
                            .id(1l)
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
                            .id(1l)
                            .day(DayEnum.SUNDAY.name())
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
            .build()

    def buildAddressDto() {
        return AddressDto.builder()
                .id(1)
                .country('Poland')
                .city('Warsaw')
                .streetName('Piękna')
                .streetNumber(1)
                .build()
    }

    def buildCuisine() {
        return CuisineDto.builder()
                .name('Greek')
                .build()
    }

    def buildBusinessHoursDto() {
        return BusinessHoursDto.builder()
                .id(1)
                .day(DayEnum.MONDAY)
                .timeFrom('10')
                .timeTo('20')
                .build()
    }
}
