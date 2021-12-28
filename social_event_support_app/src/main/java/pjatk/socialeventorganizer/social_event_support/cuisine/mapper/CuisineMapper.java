package pjatk.socialeventorganizer.social_event_support.cuisine.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine;
import pjatk.socialeventorganizer.social_event_support.cuisine.model.dto.CuisineDto;

@UtilityClass
public class CuisineMapper {

    public CuisineDto toDto(Cuisine cuisine) {
        return CuisineDto.builder()
                .name(cuisine.getName())
                .build();
    }

    public Cuisine fromDto(CuisineDto dto) {
        return Cuisine.builder()
                .name(dto.getName())
                .build();
    }
}
