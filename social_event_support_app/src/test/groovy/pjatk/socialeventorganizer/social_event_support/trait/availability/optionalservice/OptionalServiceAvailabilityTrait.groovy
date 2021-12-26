package pjatk.socialeventorganizer.social_event_support.trait.availability.optionalservice

import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService

import static pjatk.socialeventorganizer.social_event_support.availability.AvailabilityEnum.AVAILABLE

trait OptionalServiceAvailabilityTrait {

    OptionalServiceAvailability fakeOptionalServiceAvailability = OptionalServiceAvailability.builder()
            .optionalService(
                    OptionalService.builder()
                            .id(1)
                            .type("HOST")
                            .alias("ALIAS")
                            .firstName("GERALT")
                            .lastName("RIVIJSKI")
                            .description("WIEDZMIN")
                            .serviceCost(new BigDecimal("123"))
                            .email("Test@test.com")
                            .build()
            )
            .status(AVAILABLE.name())
            .build()
}