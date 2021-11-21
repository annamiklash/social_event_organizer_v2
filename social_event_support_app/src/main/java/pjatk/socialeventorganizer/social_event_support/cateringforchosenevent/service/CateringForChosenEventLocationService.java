package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.repository.CateringForLocationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CateringForChosenEventLocationService {

    private final CateringForLocationRepository cateringForLocationRepository;

    public List<CateringForChosenEventLocation> getCateringForLocationInfoByOrganizedEventIdAndLocationId(long orgEventId, long locationId) {

        return cateringForLocationRepository.findCateringForChosenEventLocationByOrganizedEventId(orgEventId, locationId).orElseGet(ArrayList::new);

    }
}
