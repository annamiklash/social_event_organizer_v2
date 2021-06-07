package pjatk.socialeventorganizer.social_event_support.invite.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationInfoResponse {

    private LocalDateTime dateTimeFrom;
    private LocalDateTime dateTimeTo;
    private String locationName;
    private String locationAddress;

    private CateringPlaceInfoResponse cateringInfo;
}
