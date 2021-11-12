package pjatk.socialeventorganizer.social_event_support.address.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto;

@Component
public class AddressMapper {

    public static AddressDto toDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .country(address.getCountry())
                .city(address.getCity())
                .streetName(address.getStreetName())
                .streetNumber(address.getStreetNumber())
                .zipCode(address.getZipCode())
                .build();
    }

    public static Address fromDto(AddressDto addressDto) {
        return Address.builder()
                .country(addressDto.getCountry())
                .city(addressDto.getCity())
                .streetName(addressDto.getStreetName())
                .streetNumber(addressDto.getStreetNumber())
                .zipCode(addressDto.getZipCode())
                .build();
    }


    public static Address updateMapToDTO(AddressDto request, long id) {
        return Address.builder()
                .id(id)
                .country(request.getCountry())
                .city(request.getCity())
                .streetName(request.getStreetName())
                .streetNumber(request.getStreetNumber())
                .zipCode(request.getZipCode())
                .build();
    }
}
