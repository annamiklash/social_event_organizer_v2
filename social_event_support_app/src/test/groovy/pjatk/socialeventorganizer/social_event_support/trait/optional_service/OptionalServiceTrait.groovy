package pjatk.socialeventorganizer.social_event_support.trait.optional_service

import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto
import pjatk.socialeventorganizer.social_event_support.businesshours.service.model.OptionalServiceBusinessHours
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.TranslationLanguageDto

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
            .address(AddressDto.builder().build())
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