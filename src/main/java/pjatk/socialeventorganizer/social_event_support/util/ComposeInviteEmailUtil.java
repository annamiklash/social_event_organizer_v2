package pjatk.socialeventorganizer.social_event_support.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import pjatk.socialeventorganizer.social_event_support.invite.InviteContent;
import pjatk.socialeventorganizer.social_event_support.invite.response.GuestInfoResponse;
import pjatk.socialeventorganizer.social_event_support.invite.response.LocationInfoResponse;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@UtilityClass
public class ComposeInviteEmailUtil implements Serializable {

    public String composeEmail(GuestInfoResponse guest, InviteContent inviteContent) {

        String content = new StringBuilder()
                .append("Dear ")
                .append(guest.getGuestFirstAndLastName())
                .append(", ")
                .append("we are pleased to inform that You have been invited to a ")
                .append(inviteContent.getEventInfo().getEventType())
                .append(" organized by ")
                .append(inviteContent.getOrganizerInfo().getFirstAndLastName())
                .append(". Below you can find a schedule so You are better prepared to the upcoming occasion!")
                .append("\n\n")
                .append(inviteContent.getEventInfo().getEventName())
                .append("\n")
                .append("Beginning date and time: ")
                .append(inviteContent.getEventInfo().getStartDate())
                .append("\n")
                .toString();
        boolean isOneDay = isOneDayEvent(inviteContent.getEventInfo().getEndDate());
        if (!isOneDay) {
            content = content.concat("Ending date and time: ")
                    .concat(inviteContent.getEventInfo().getEndDate().toString())
                    .concat("\n");
        }

        final List<LocationInfoResponse> locations = inviteContent.getLocationInfo();
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
        }

        content = content.concat("\nIn case of any questions do not hesitate to ask ")
                .concat(inviteContent.getOrganizerInfo().getFirstAndLastName())
                .concat(" at ")
                .concat(inviteContent.getOrganizerInfo().getEmail())
                .concat(" or calling at ")
                .concat(inviteContent.getOrganizerInfo().getPhoneNumber().toString())
                .concat("\n\n\n")
                .concat("Sent via SocialEventOrganizer app");

        log.info("INVITE \n:" + content);
        return content;
    }

    private boolean isOneDayEvent(LocalDate endDate) {
        return endDate == null;
    }

}
