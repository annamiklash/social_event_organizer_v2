package pjatk.socialeventorganizer.social_event_support.location.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.Location;
import pjatk.socialeventorganizer.social_event_support.location.model.request.LocationRequest;
import pjatk.socialeventorganizer.social_event_support.location.model.response.LocationInformationResponse;
import pjatk.socialeventorganizer.social_event_support.location.model.response.LocationResponse;

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
                .descriptions(new HashSet<>())
                .images(new HashSet<>())
                .build();
    }

    public LocationResponse mapToResponse(Location location) {
        return LocationResponse.builder()
                .id(location.getId())
                .build();
    }

    public LocationInformationResponse mapDTOtoInformationResponse(Location location) {
        return LocationInformationResponse.builder()
                .id(Math.toIntExact(location.getId()))
                .name(location.getName())
                .description(location.getDescription())
                .email(location.getEmail())
                .phoneNumber(location.getPhoneNumber())
                .seatingCapacity(location.getSeatingCapacity())
                .standingCapacity(location.getStandingCapacity())
                .dailyRentCost(location.getDailyRentCost())
                .sizeInSqMeters(location.getSizeSqMeters())
                .locationAddress(location.getLocationAddress())
                .build();
    }
}
