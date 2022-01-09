package pjatk.socialeventorganizer.social_event_support.event.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper.CateringForChosenLocationMapper;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.customer.guest.mapper.GuestMapper;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventConfirmationDto;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.mapper.LocationForEventMapper;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.mapper.OptionalServiceForLocationMapper;

import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.IN_PROGRESS;

@UtilityClass
public class OrganizedEventMapper {

    public OrganizedEventDto toDto(OrganizedEvent organizedEvent) {
        return OrganizedEventDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .guestCount(organizedEvent.getGuestCount())
                .date(DateTimeUtil.fromLocalDateToDateString(organizedEvent.getDate()))
                .startTime(DateTimeUtil.fromLocalTimeToTimeString(organizedEvent.getStartTime()))
                .endTime(DateTimeUtil.fromLocalTimeToTimeString(organizedEvent.getEndTime()))
                .eventStatus(organizedEvent.getEventStatus())
                .eventType(EventTypeMapper.toDto(organizedEvent.getEventType()).getType())
                .createdAt(DateTimeUtil.fromLocalDateTimetoString(organizedEvent.getCreatedAt()))
                .modifiedAt(DateTimeUtil.fromLocalDateTimetoString(organizedEvent.getModifiedAt()))
                .build();
    }

    public OrganizedEventDto toDtoWithCustomerAndEventType(OrganizedEvent organizedEvent) {
        final OrganizedEventDto dto = toDto(organizedEvent);
        dto.setCustomer(CustomerMapper.toDto(organizedEvent.getCustomer()));
        dto.setEventType(organizedEvent.getEventType().getType());
        dto.setLocation(LocationForEventMapper.toDto(organizedEvent.getLocationForEvent()));

        return dto;
    }

    public OrganizedEventDto toDtoWithCustomer(OrganizedEvent organizedEvent) {
        return OrganizedEventDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .date(DateTimeUtil.fromLocalDateToDateString(organizedEvent.getDate()))
                .startTime(DateTimeUtil.fromLocalTimeToTimeString(organizedEvent.getStartTime()))
                .endTime(DateTimeUtil.fromLocalTimeToTimeString(organizedEvent.getEndTime()))
                .guestCount(organizedEvent.getGuestCount())
                .eventStatus(organizedEvent.getEventStatus())
                .eventType(EventTypeMapper.toDto(organizedEvent.getEventType()).getType())
                .customer(CustomerMapper.toDto(organizedEvent.getCustomer()))
                .createdAt(DateTimeUtil.fromLocalDateTimetoString(organizedEvent.getCreatedAt()))
                .modifiedAt(DateTimeUtil.fromLocalDateTimetoString(organizedEvent.getModifiedAt()))
                .build();
    }

    public OrganizedEventConfirmationDto toDtoWithLocation(OrganizedEvent organizedEvent) {
        return OrganizedEventConfirmationDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .eventType(organizedEvent.getEventType().getType())
                .eventStatus(organizedEvent.getEventStatus())
                .locationForEvent(LocationForEventMapper.toDto(organizedEvent.getLocationForEvent()))
                .build();
    }

    public OrganizedEventConfirmationDto toDtoWithServices(OrganizedEvent organizedEvent) {
        return OrganizedEventConfirmationDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .eventType(organizedEvent.getEventType().getType())
                .eventStatus(organizedEvent.getEventStatus())
                .locationForEvent(LocationForEventMapper.toDto(organizedEvent.getLocationForEvent()))
                .optionalServices(organizedEvent.getLocationForEvent().getServices().stream()
                        .map(OptionalServiceForLocationMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public OrganizedEventConfirmationDto toDtoWithCatering(OrganizedEvent organizedEvent) {
        return OrganizedEventConfirmationDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .eventType(organizedEvent.getEventType().getType())
                .eventStatus(organizedEvent.getEventStatus())
                .locationForEvent(LocationForEventMapper.toDto(organizedEvent.getLocationForEvent()))
                .catering(organizedEvent.getLocationForEvent().getCateringsForEventLocation().stream()
                        .map(CateringForChosenLocationMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }


    public static OrganizedEventDto toDtoWithDetail(OrganizedEvent organizedEvent) {
        final OrganizedEventDto dto = toDto(organizedEvent);
        dto.setCustomer(CustomerMapper.toDto(organizedEvent.getCustomer()));
        dto.setEventType(organizedEvent.getEventType().getType());
        dto.setLocation(LocationForEventMapper.toDtoWithDetail(organizedEvent.getLocationForEvent()));
        dto.setGuests(organizedEvent.getGuests().stream()
                .map(GuestMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public OrganizedEventDto toDtoForInvite(OrganizedEvent organizedEvent) {
        return OrganizedEventDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .date(DateTimeUtil.fromLocalDateToDateString(organizedEvent.getDate()))
                .startTime(DateTimeUtil.fromLocalTimeToTimeString(organizedEvent.getStartTime()))
                .endTime(DateTimeUtil.fromLocalTimeToTimeString(organizedEvent.getStartTime()))
                .eventType(EventTypeMapper.toDto(organizedEvent.getEventType()).getType())
                .customer(CustomerMapper.toDto(organizedEvent.getCustomer()))
                .guests(organizedEvent.getGuests().stream().map(GuestMapper::toDto).collect(Collectors.toList()))
                .location(LocationForEventMapper.toDtoWithCatering(organizedEvent.getLocationForEvent()))
                .build();

    }

    public OrganizedEvent fromDto(OrganizedEventDto dto) {
        return OrganizedEvent.builder()
                .name(dto.getName())
                .date(DateTimeUtil.fromStringToFormattedDate(dto.getDate()))
                .startTime(DateTimeUtil.fromTimeStringToLocalTime(dto.getStartTime()))
                .endTime(DateTimeUtil.fromTimeStringToLocalTime(dto.getEndTime()))
                .guestCount(dto.getGuestCount())
                .eventStatus(IN_PROGRESS.name())
                .build();
    }


}
