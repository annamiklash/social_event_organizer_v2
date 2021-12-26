package pjatk.socialeventorganizer.social_event_support.address.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;

@UtilityClass
public class AddressMapper {

    public static AddressDto toDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .country(address.getCountry())
                .city(address.getCity())
                .streetName(address.getStreetName())
                .streetNumber(address.getStreetNumber())
                .zipCode(address.getZipCode())
                .createdAt(DateTimeUtil.toStringFromLocalDateTime(address.getCreatedAt()))
                .modifiedAt(DateTimeUtil.toStringFromLocalDateTime(address.getModifiedAt()))
                .deletedAt(DateTimeUtil.toStringFromLocalDateTime(address.getDeletedAt()))
                .build();
    }

    public static Address fromDto(AddressDto addressDto) {
        return Address.builder()
                .id(addressDto.getId())
                .country(addressDto.getCountry())
                .city(addressDto.getCity())
                .streetName(addressDto.getStreetName())
                .streetNumber(addressDto.getStreetNumber())
                .zipCode(addressDto.getZipCode())
                .build();
    }
}
