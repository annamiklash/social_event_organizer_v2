package pjatk.socialeventorganizer.social_event_support.trait.optional_service

import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService

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
            .build()
}