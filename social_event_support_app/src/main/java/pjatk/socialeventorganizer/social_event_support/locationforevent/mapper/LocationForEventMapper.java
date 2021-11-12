package pjatk.socialeventorganizer.social_event_support.locationforevent.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.dto.LocationForEventDto;

@UtilityClass
public class LocationForEventMapper {

    public LocationForEventDto toDto(LocationForEvent location) {
        return LocationForEventDto.builder()
                .id(location.getId())
                .dateTimeFrom(String.valueOf(location.getDateTimeFrom()))
                .dateTimeTo(String.valueOf(location.getDatTimeTo()))
                //TODO:location
                .build();
    }

}
