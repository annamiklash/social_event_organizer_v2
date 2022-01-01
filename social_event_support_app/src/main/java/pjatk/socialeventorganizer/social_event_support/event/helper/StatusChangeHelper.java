package pjatk.socialeventorganizer.social_event_support.event.helper;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.enums.ConfirmationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;

import java.util.Set;

import static pjatk.socialeventorganizer.social_event_support.enums.EventStatusEnum.IN_PROGRESS;

@Component
public class StatusChangeHelper {

    public boolean possibleToChangeStatusFromInProgressToConfirmed(OrganizedEvent organizedEvent) {
        final String eventStatus = organizedEvent.getEventStatus();
        final LocationForEvent locationForEvent = organizedEvent.getLocationForEvent();

        if (!IN_PROGRESS.name().equals(eventStatus)
                || locationForEvent == null
                || ConfirmationStatusEnum.NOT_CONFIRMED.name().equals(locationForEvent.getConfirmationStatus())) {
            return false;
        }

        return areAllCateringForEventHaveConfirmedStatus(locationForEvent.getCateringsForEventLocation())
                && areAllServicesHaveConfirmedStatus(locationForEvent.getServices());

    }

    private boolean areAllServicesHaveConfirmedStatus(Set<OptionalServiceForChosenLocation> services) {
        if (CollectionUtils.isEmpty(services)) {
            return true;
        }
        return services
                .stream()
                .allMatch(service -> ConfirmationStatusEnum.CONFIRMED.name().equals(service.getConfirmationStatus()));
    }

    private boolean areAllCateringForEventHaveConfirmedStatus(Set<CateringForChosenEventLocation> cateringsForEventLocation) {
        if (CollectionUtils.isEmpty(cateringsForEventLocation)) {
            return true;
        }
        return cateringsForEventLocation
                .stream()
                .allMatch(catering -> ConfirmationStatusEnum.CONFIRMED.name().equals(catering.getConfirmationStatus()));
    }
}
