package pjatk.socialeventorganizer.social_event_support.trait.catering

import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine

trait CuisineTrait {

    Cuisine faceCuisine = Cuisine.builder()
            .id(1)
            .name('Greek')
            .build();

    Cuisine faceCuisineDto = CuisineDto.builder()
            .name('Greek')
            .build();
}