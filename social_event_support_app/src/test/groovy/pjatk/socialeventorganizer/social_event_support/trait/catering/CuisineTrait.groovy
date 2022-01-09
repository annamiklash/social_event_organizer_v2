package pjatk.socialeventorganizer.social_event_support.trait.catering

import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine
import pjatk.socialeventorganizer.social_event_support.cuisine.model.dto.CuisineDto

trait CuisineTrait {

    Cuisine fakeCuisine = Cuisine.builder()
            .id(1L)
            .name('Greek')
            .build()

    CuisineDto fakeCuisineDto = CuisineDto.builder()
            .id(1L)
            .name('Greek')
            .build()
}