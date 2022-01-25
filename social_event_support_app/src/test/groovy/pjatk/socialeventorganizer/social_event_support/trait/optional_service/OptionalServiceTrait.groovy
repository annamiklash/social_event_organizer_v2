package pjatk.socialeventorganizer.social_event_support.trait.optional_service

import pjatk.socialeventorganizer.social_event_support.address.model.Address
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability
import pjatk.socialeventorganizer.social_event_support.business.model.Business
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto
import pjatk.socialeventorganizer.social_event_support.businesshours.service.model.OptionalServiceBusinessHours
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.TranslationLanguageDto
import pjatk.socialeventorganizer.social_event_support.optional_service.model.other.Host

trait OptionalServiceTrait {

    OptionalService fakeOptionalService = OptionalService.builder()
            .id(1)
            .type("HOST")
            .alias("ALIAS")
            .firstName("GERALT")
            .lastName("RIVIJSKI")
            .description("WIEDZMIN")
            .serviceCost(new BigDecimal("123"))
            .email('email@email.com')
            .optionalServiceBusinessHours(new HashSet<OptionalServiceBusinessHours>())
            .availability(new HashSet<OptionalServiceAvailability>())
            .business(Business.builder()
                    .id(1)
                    .firstName('Name')
                    .lastName('Name')
                    .businessName('Name')
                    .verificationStatus('VERIFIED')
                    .phoneNumber(new BigInteger("123123123"))
                    .build())
            .serviceAddress(Address.builder()
                    .id(1)
                    .country("Coutry")
                    .city("City")
                    .streetName("Street")
                    .streetNumber(1)
                    .zipCode("01-157")
                    .build())
            .build()

    Host fakeOptionalHost = Host.builder()
            .id(1)
            .type("HOST")
            .alias("ALIAS")
            .firstName("GERALT")
            .lastName("RIVIJSKI")
            .description("WIEDZMIN")
            .serviceCost(new BigDecimal("123"))
            .email('email@email.com')
            .optionalServiceBusinessHours(new HashSet<OptionalServiceBusinessHours>())
            .serviceAddress(Address.builder()
                    .id(1)
                    .country("Coutry")
                    .city("City")
                    .streetName("Street")
                    .streetNumber(1)
                    .zipCode("01-157")
                    .build())
            .build()

    OptionalServiceDto fakeOptionalServiceHostDto = OptionalServiceDto.builder()
            .id(1)
            .type("HOST")
            .alias("ALIAS")
            .firstName("GERALT")
            .lastName("RIVIJSKI")
            .description("WIEDZMIN")
            .serviceCost("123")
            .email('email@email.com')
            .businessHours(new ArrayList<BusinessHoursDto>())
            .address(AddressDto.builder()
                    .id(1)
                    .country("Coutry")
                    .city("City")
                    .streetName("Street")
                    .streetNumber(1)
                    .zipCode("01-157")
                    .build())
            .build()

    OptionalServiceDto fakeOptionalServiceInterpreterDto = OptionalServiceDto.builder()
            .id(1)
            .type("INTERPRETER")
            .alias("ALIAS")
            .firstName("GERALT")
            .lastName("RIVIJSKI")
            .description("WIEDZMIN")
            .serviceCost("123")
            .email('email@email.com')
            .businessHours(new ArrayList<BusinessHoursDto>())
            .address(AddressDto.builder().build())
            .translationLanguages(
                    Set.of(TranslationLanguageDto.builder()
                            .name('English')
                            .build()))
            .build()
}