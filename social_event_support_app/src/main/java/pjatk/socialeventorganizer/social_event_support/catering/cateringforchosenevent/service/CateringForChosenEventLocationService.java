package pjatk.socialeventorganizer.social_event_support.catering.cateringforchosenevent.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.catering.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.catering.cateringforchosenevent.repository.CateringForLocationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CateringForChosenEventLocationService {

    private final CateringForLocationRepository cateringForLocationRepository;

    public List<CateringForChosenEventLocation> getCateringForLocationInfoByOrganizedEventIdAndLocationId(long orgEventId, long locationId) {
        final Optional<List<CateringForChosenEventLocation>> optionalCateringList =
                cateringForLocationRepository.findCateringForChosenEventLocationByOrganizedEventId(orgEventId, locationId);

        return optionalCateringList.orElseGet(ArrayList::new);

    }
}
