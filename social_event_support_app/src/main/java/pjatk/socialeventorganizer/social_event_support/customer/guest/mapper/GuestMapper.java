package pjatk.socialeventorganizer.social_event_support.customer.guest.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;

@UtilityClass
public class GuestMapper {

    public GuestDto toDto(Guest guest) {
        return GuestDto.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .createdAt(String.valueOf(guest.getCreatedAt()))
                .modifiedAt(String.valueOf(guest.getModifiedAt()))
                .build();
    }


    public GuestDto toDtoWithCustomer(Guest guest) {
        return GuestDto.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .createdAt(String.valueOf(guest.getCreatedAt()))
                .modifiedAt(String.valueOf(guest.getModifiedAt()))
                .customer(CustomerMapper.toDto(guest.getCustomer()))
                .build();
    }

    public Guest fromDto(GuestDto dto) {
        return Guest.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .build();

    }
}
