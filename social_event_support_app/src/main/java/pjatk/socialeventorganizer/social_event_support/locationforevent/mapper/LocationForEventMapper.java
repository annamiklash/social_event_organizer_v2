package pjatk.socialeventorganizer.social_event_support.locationforevent.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper.CateringForChosenLocationMapper;
import pjatk.socialeventorganizer.social_event_support.customer.guest.mapper.GuestMapper;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.location.mapper.LocationMapper;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.locationforevent.model.dto.LocationForEventDto;

import java.util.stream.Collectors;

@UtilityClass
public class LocationForEventMapper {

    public LocationForEventDto toDto(LocationForEvent location) {
        return LocationForEventDto.builder()
                .id(location.getId())
                .timeFrom(String.valueOf(location.getDateTimeFrom()))
                .timeTo(String.valueOf(location.getDateTimeTo()))
                .guestsCount(location.getGuestCount())
                .confirmationStatus(location.getConfirmationStatus())
                .location(LocationMapper.toDto(location.getLocation()))
                .build();
    }

    public static LocationForEventDto toDtoWithLocationAndEvent(LocationForEvent location) {
        final LocationForEventDto dto = toDto(location);
        dto.setEvent(OrganizedEventMapper.toDtoWithCustomer(location.getEvent()));

        return dto;
    }

    public static LocationForEventDto toDtoWithGuestsAndCatering(LocationForEvent location) {
        final LocationForEventDto dto = toDto(location);
        dto.setGuests(location.getGuests().stream().map(GuestMapper::toDto).collect(Collectors.toList()));

        //TODO: create DTO for cateringForLocation
        dto.setCatering(location.getCateringsForEventLocation().stream()
                .map(CateringForChosenLocationMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }
}
