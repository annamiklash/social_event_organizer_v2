package pjatk.socialeventorganizer.social_event_support.location.locationforevent.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper.CateringForChosenLocationMapper;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto.LocationForEventDto;
import pjatk.socialeventorganizer.social_event_support.location.mapper.LocationMapper;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.mapper.OptionalServiceForLocationMapper;

import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.location.locationforevent.enums.ConfirmationStatusEnum.NOT_CONFIRMED;

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

    public static LocationForEventDto toDtoWithCatering(LocationForEvent location) {
        final LocationForEventDto dto = toDto(location);

        dto.setCaterings(location.getCateringsForEventLocation().stream()
                .map(CateringForChosenLocationMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public static LocationForEventDto toDtoWithDetail(LocationForEvent location) {
        final LocationForEventDto dto = toDto(location);

        dto.setCaterings(location.getCateringsForEventLocation().stream()
                .map(CateringForChosenLocationMapper::toDto)
                .collect(Collectors.toList()));

        dto.setOptionalServices(location.getServices().stream()
                .map(OptionalServiceForLocationMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public LocationForEvent fromDto(LocationForEventDto dto) {
        return LocationForEvent.builder()
                .guestCount(dto.getGuestsCount())
                .dateTimeFrom(DateTimeUtil.fromStringToFormattedDateTime(dto.getTimeFrom()))
                .dateTimeTo(DateTimeUtil.fromStringToFormattedDateTime(dto.getTimeTo()))
                .confirmationStatus(NOT_CONFIRMED.toString())
                .build();
    }
}
