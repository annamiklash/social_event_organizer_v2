package pjatk.socialeventorganizer.social_event_support.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.model.dto.Location;
import pjatk.socialeventorganizer.social_event_support.model.request.LocationRequest;
import pjatk.socialeventorganizer.social_event_support.model.response.LocationResponse;

import java.util.HashSet;

@Component
public class LocationMapper {

    public Location mapToDTO(LocationRequest request, Address address) {
        return Location.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(Converter.convertPhoneNumberString(request.getPhoneNumber()))
                .seatingCapacity(request.getSeatingCapacity())
                .standingCapacity(request.getStandingCapacity())
                .description(Converter.convertDescriptionsString(request.getDescription()))
                .dailyRentCost(Converter.convertPriceString(request.getDailyRentCost()))
                .sizeSqMeters(request.getSizeSqMeters())
                .locationAddress(address)
                .businessId(request.getBusinessId())
                .descriptions(new HashSet<>())
                .images(new HashSet<>())
                .build();
    }

    public LocationResponse mapToResponse(Location location) {
        return LocationResponse.builder()
                .id(location.getId())
                .build();
    }
}
