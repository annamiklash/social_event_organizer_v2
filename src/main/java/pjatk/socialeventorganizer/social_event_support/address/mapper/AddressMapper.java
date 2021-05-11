package pjatk.socialeventorganizer.social_event_support.address.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.request.AddressRequest;
import pjatk.socialeventorganizer.social_event_support.address.model.response.AddressResponse;

@Component
public class AddressMapper {


    public Address mapToDTO(AddressRequest request) {
        return Address.builder()
                .country(request.getCounty())
                .city(request.getCity())
                .streetName(request.getStreetName())
                .streetNumber(request.getStreetNumber())
                .zipCode(request.getZipCode())
                .build();
    }

    public AddressResponse mapToResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .build();
    }

    public Address updateMapToDTO(AddressRequest request, long id) {
        return Address.builder()
                .id(id)
                .country(request.getCounty())
                .city(request.getCity())
                .streetName(request.getStreetName())
                .streetNumber(request.getStreetNumber())
                .zipCode(request.getZipCode())
                .build();
    }
}
