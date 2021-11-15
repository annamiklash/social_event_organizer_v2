package pjatk.socialeventorganizer.social_event_support.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import pjatk.socialeventorganizer.social_event_support.invite.EventInvitationDto;
import pjatk.socialeventorganizer.social_event_support.invite.response.CateringPlaceInfoResponse;
import pjatk.socialeventorganizer.social_event_support.invite.response.GuestInfoResponse;
import pjatk.socialeventorganizer.social_event_support.invite.response.LocationInfoResponse;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@UtilityClass
public class ComposeInviteEmailUtil implements Serializable {

    public String composeEmail(GuestInfoResponse guest, EventInvitationDto eventInvitationDto) {

        String content = new StringBuilder()
                .append("Dear ")
                .append(guest.getGuestFirstAndLastName())
                .append(",\n")
                .append("We are pleased to inform that You have been invited to a ")
                .append(eventInvitationDto.getEventInfo().getEventType())
                .append(" organized by ")
                .append(eventInvitationDto.getOrganizerInfo().getFirstAndLastName())
                .append(". Below You can find a schedule so You are better prepared to the upcoming occasion!")
                .append("\n\n")
                .append(eventInvitationDto.getEventInfo().getEventName())
                .append("\n")
                .append("Beginning date and time: ")
                .append(eventInvitationDto.getEventInfo().getStartDate())
                .append("\n")
                .toString();
        boolean isOneDay = isOneDayEvent(eventInvitationDto.getEventInfo().getEndDate());
        if (!isOneDay) {
            content = content.concat("Ending date and time: ")
                    .concat(eventInvitationDto.getEventInfo().getEndDate().toString())
                    .concat("\n");
        }

        final List<LocationInfoResponse> locations = eventInvitationDto.getLocationInfo();
        for (LocationInfoResponse location : locations) {
            content = content
                    .concat("When and where: ")
                    .concat(location.getLocationName())
                    .concat(" located at ")
                    .concat(location.getLocationAddress())
                    .concat(" on ")
                    .concat(location.getDateTimeFrom().toLocalDate().toString())
                    .concat(" at ")
                    .concat(location.getDateTimeFrom().toLocalTime().toString())
                    .concat("\n");
            final List<CateringPlaceInfoResponse> caterings = location.getCateringOrders();
            if (caterings != null && caterings.size() > 0) {
                for (CateringPlaceInfoResponse catering : caterings) {
                    content = content.concat("\t")
                            .concat("Meals and snacks provided by: ")
                            .concat(catering.getCateringName())
                            .concat(" will be served around ")
                            .concat(catering.getDateTime().toLocalTime().toString())
                            .concat("\n");
                }
                content = content.concat("\n");
            }
        }

        content = content.concat("\nIn case of any questions do not hesitate to ask ")
                .concat(eventInvitationDto.getOrganizerInfo().getFirstAndLastName())
                .concat(" at ")
                .concat(eventInvitationDto.getOrganizerInfo().getEmail())
                .concat(" or calling at ")
                .concat(eventInvitationDto.getOrganizerInfo().getPhoneNumber().toString())
                .concat("\n\n\n")
                .concat("Sent via SocialEventOrganizer app");

        log.info("INVITE \n:" + content);
        return content;
    }

    private boolean isOneDayEvent(LocalDate endDate) {
        return endDate == null;
    }

}
