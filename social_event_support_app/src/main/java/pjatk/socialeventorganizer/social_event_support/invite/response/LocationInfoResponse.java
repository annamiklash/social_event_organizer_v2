package pjatk.socialeventorganizer.social_event_support.invite.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationInfoResponse {


    private Long locationId;
    private LocalDateTime dateTimeFrom;
    private LocalDateTime dateTimeTo;
    private String locationName;
    private String locationAddress;

    private List<CateringPlaceInfoResponse> cateringOrders = new ArrayList<>();
}
