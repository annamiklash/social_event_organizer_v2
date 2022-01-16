package pjatk.socialeventorganizer.social_event_support.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto;
import pjatk.socialeventorganizer.social_event_support.event.model.dto.OrganizedEventDto;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto.LocationForEventDto;

import java.io.Serializable;
import java.util.List;

@Slf4j
@UtilityClass
public class ComposeInviteEmailUtil implements Serializable {

    public String composeEmail(GuestDto guest, OrganizedEventDto dto) {

        String content = new StringBuilder()
                .append("Dear ").append(guest.getFirstName()).append(" ").append(guest.getLastName())
                .append(",\n")
                .append("We are pleased to inform that You have been invited to a ")
                .append(dto.getEventType())
                .append(" organized by ").append(dto.getCustomer().getFirstName()).append(" ").append(dto.getCustomer().getLastName())
                .append(". Below You can find a schedule so You are better prepared to the upcoming occasion!")
                .append("\n\n")
                .append(dto.getName())
                .append("\n")
                .append("Beginning date and time: ")
                .append(dto.getDate())
                .append("\n")
                .toString();

        final LocationForEventDto locationForEventDto = dto.getLocation().stream()
                .filter(location -> !"CANCELLED".equals(location.getConfirmationStatus()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No current reservation"));

        content = content
                .concat("When and where: ")
                .concat(locationForEventDto.getLocation().getName())
                .concat(" located at ")
                .concat(getAddressString(locationForEventDto.getLocation().getAddress()))
                .concat(" on ")
                .concat(dto.getStartTime())
                .concat(" from ")
                .concat(locationForEventDto.getTimeFrom())
                .concat(" until ")
                .concat(locationForEventDto.getTimeTo())
                .concat("\n");

        final List<CateringForChosenEventLocationDto> caterings = locationForEventDto.getCaterings();
        if (caterings != null && caterings.size() > 0) {
            for (CateringForChosenEventLocationDto catering : caterings) {
                content = content.concat("\t")
                        .concat("Meals and snacks provided by: ")
                        .concat(catering.getCatering().getName())
                        .concat(" will be served around ")
                        .concat(catering.getTime())
                        .concat("\n");
            }
            content = content.concat("\n");
        }

        content = content.concat("\nIn case of any questions do not hesitate to ask ")
                .concat(dto.getCustomer().getFirstName() + " " + dto.getCustomer().getLastName())
                .concat(" at ")
                .concat(dto.getCustomer().getUser().getEmail())
                .concat(" or calling at ")
                .concat(dto.getCustomer().getPhoneNumber())
                .concat("\n\n\n")
                .concat("Sent via SocialEventOrganizer app");

        log.info("INVITE \n:" + content);
        return content;
    }

    private String getAddressString(AddressDto dto) {
        return dto.getCountry() + ", " + dto.getCity() + " " + dto.getStreetName() + " " + dto.getStreetNumber() + " " + dto.getZipCode();
    }

}
