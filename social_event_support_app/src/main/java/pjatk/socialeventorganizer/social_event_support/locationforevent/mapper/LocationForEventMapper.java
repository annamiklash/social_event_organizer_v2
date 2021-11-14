package pjatk.socialeventorganizer.social_event_support.locationforevent.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.location.mapper.LocationMapper;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.dto.LocationForEventDto;

@UtilityClass
public class LocationForEventMapper {

    public LocationForEventDto toDto(LocationForEvent location) {
        return LocationForEventDto.builder()
                .id(location.getId())
                .timeFrom(String.valueOf(location.getDateTimeFrom()))
                .timeTo(String.valueOf(location.getDateTimeTo()))
                .guests(location.getGuests())
                .confirmationStatus(location.getConfirmationStatus())
                .location(LocationMapper.toDto(location.getLocation()))
                .build();
    }

    public static LocationForEventDto toDtoWithLocationAndEvent(LocationForEvent location) {
        final LocationForEventDto dto = toDto(location);
        dto.setEvent(OrganizedEventMapper.toDtoWithCustomer(location.getEvent()));

        return dto;
    }
}
