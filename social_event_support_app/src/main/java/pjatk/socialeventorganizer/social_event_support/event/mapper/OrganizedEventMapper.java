package pjatk.socialeventorganizer.social_event_support.event.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.customer.guest.mapper.GuestMapper;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventConfirmationDto;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.mapper.LocationForEventMapper;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;

import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.IN_PROGRESS;

@UtilityClass
public class OrganizedEventMapper {

    public OrganizedEventDto toDto(OrganizedEvent organizedEvent) {
        return OrganizedEventDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .startDateTime(DateTimeUtil.toStringFromLocalDateTime(organizedEvent.getStartDateTime()))
                .endDateTime(DateTimeUtil.toStringFromLocalDateTime(organizedEvent.getStartDateTime()))
                .eventStatus(organizedEvent.getEventStatus())
                .eventType(EventTypeMapper.toDto(organizedEvent.getEventType()).getType())
                .createdAt(DateTimeUtil.toStringFromLocalDateTime(organizedEvent.getStartDateTime()))
                .modifiedAt(DateTimeUtil.toStringFromLocalDateTime(organizedEvent.getModifiedAt()))
                .build();
    }

    public OrganizedEventDto toDtoWithCustomer(OrganizedEvent organizedEvent) {
        return OrganizedEventDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .startDateTime(DateTimeUtil.toStringFromLocalDateTime(organizedEvent.getStartDateTime()))
                .endDateTime(DateTimeUtil.toStringFromLocalDateTime(organizedEvent.getStartDateTime()))
                .eventStatus(organizedEvent.getEventStatus())
                .eventType(EventTypeMapper.toDto(organizedEvent.getEventType()).getType())
                .customer(CustomerMapper.toDto(organizedEvent.getCustomer()))
                .createdAt(DateTimeUtil.toStringFromLocalDateTime(organizedEvent.getStartDateTime()))
                .modifiedAt(DateTimeUtil.toStringFromLocalDateTime(organizedEvent.getModifiedAt()))
                .build();
    }

    public OrganizedEventConfirmationDto toOrganizedConfirmationDto(LocationForEvent locationForEvent) {
        return OrganizedEventConfirmationDto.builder()
                .id(locationForEvent.getEvent().getId())
                .name(locationForEvent.getEvent().getName())
                .eventType(locationForEvent.getEvent().getEventType().getType())
                .eventStatus(locationForEvent.getEvent().getEventStatus())
                .locationForEvent(LocationForEventMapper.toDto(locationForEvent))
                .build();

    }

    public OrganizedEventDto toDtoForInvite(OrganizedEvent organizedEvent) {
        return OrganizedEventDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .startDateTime(DateTimeUtil.toStringFromLocalDateTime(organizedEvent.getStartDateTime()))
                .endDateTime(DateTimeUtil.toStringFromLocalDateTime(organizedEvent.getStartDateTime()))
                .eventType(EventTypeMapper.toDto(organizedEvent.getEventType()).getType())
                .customer(CustomerMapper.toDto(organizedEvent.getCustomer()))
                .guests(organizedEvent.getGuests().stream().map(GuestMapper::toDto).collect(Collectors.toList()))
                .location(LocationForEventMapper.toDtoWithCatering(organizedEvent.getLocationForEvent()))
                .build();

    }

    public OrganizedEvent fromDto(OrganizedEventDto dto) {
        return OrganizedEvent.builder()
                .name(dto.getName())
                .startDateTime(DateTimeUtil.fromStringToFormattedDateTime(dto.getStartDateTime()))
                .endDateTime(DateTimeUtil.fromStringToFormattedDateTime(dto.getEndDateTime()))
                .guestCount(dto.getGuestCount())
                .eventStatus(IN_PROGRESS.name())
                .build();
    }
}
