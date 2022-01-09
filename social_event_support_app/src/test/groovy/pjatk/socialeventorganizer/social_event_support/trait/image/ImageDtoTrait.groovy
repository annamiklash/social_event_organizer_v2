package pjatk.socialeventorganizer.social_event_support.trait.image

import com.google.common.collect.ImmutableList
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto
import pjatk.socialeventorganizer.social_event_support.cuisine.model.dto.CuisineDto
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto

trait ImageDtoTrait {

    ImageDto fakeImageDto = ImageDto.builder()
            .id(1L)
            .path("sample/path/to/image")
            .name('sample_image_name')
            .isMain(true)
            .encodedImage("sample_encoded_image")
            .location(LocationDto.builder()
                    .id(1)
                    .name('Name')
                    .email('email@email.com')
                    .build())
            .catering(CateringDto.builder()
                    .id(1L)
                    .name('Name')
                    .email('email@email.com')
                    .phoneNumber('123456789')
                    .description('description')
                    .address(AddressDto.builder()
                            .id(1L)
                            .country('Poland')
                            .city('Warsaw')
                            .streetName('Piękna')
                            .streetNumber(1)
                            .zipCode('01-157')
                            .build())
                    .cuisines(ImmutableList.of(CuisineDto.builder()
                            .id(1)
                            .name('Greek')
                            .build()))
                    .build())
            .service(OptionalServiceDto.builder().id(1L).build())
            .build()

}