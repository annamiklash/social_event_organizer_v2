package pjatk.socialeventorganizer.social_event_support.invite.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.invite.response.LocationInfoResponse;
import pjatk.socialeventorganizer.social_event_support.locationforevent.LocationForEvent;

@Component
public class LocationInfoMapper {

    public LocationInfoResponse mapToResponse(LocationForEvent locationForEvent) {
        final Address locationAddress = locationForEvent.getLocation().getLocationAddress();
        final String whiteSpace = " ";
        final String locationAddressString = new StringBuilder()
                .append(locationAddress.getStreetName())
                .append(whiteSpace)
                .append(locationAddress.getStreetNumber())
                .append(whiteSpace)
                .append(locationAddress.getCity())
                .append(whiteSpace)
                .append(locationAddress.getCountry())
                .append(whiteSpace)
                .append(locationAddress.getZipCode())
                .toString();

        return LocationInfoResponse.builder()
                .locationName(locationForEvent.getLocation().getName())
                .locationAddress(locationAddressString)
                .dateTimeFrom(locationForEvent.getDateTimeFrom())
                .dateTimeTo(locationForEvent.getDatTimeTo())
                .locationId(locationForEvent.getLocation().getId())
                .build();
    }
}
