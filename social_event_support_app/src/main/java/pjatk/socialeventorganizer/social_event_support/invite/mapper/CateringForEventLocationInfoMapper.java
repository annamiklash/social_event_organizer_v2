package pjatk.socialeventorganizer.social_event_support.invite.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.dto.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.invite.response.CateringPlaceInfoResponse;

@Component
public class CateringForEventLocationInfoMapper {

    public CateringPlaceInfoResponse mapToResponse(CateringForChosenEventLocation cateringForEventLocation) {
        return CateringPlaceInfoResponse.builder()
                .dateTime(cateringForEventLocation.getDateTime())
                .cateringName(cateringForEventLocation.getCatering().getName())
                .build();
    }
}
