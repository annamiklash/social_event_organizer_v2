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
public class ServicesInfoResponse {

    private Long optionalServiceId;
    private String comment;
    private String serviceType;
    private String serviceAlias;
    private LocalDateTime dateTimeFrom;
    private LocalDateTime dateTimeTo;
}
