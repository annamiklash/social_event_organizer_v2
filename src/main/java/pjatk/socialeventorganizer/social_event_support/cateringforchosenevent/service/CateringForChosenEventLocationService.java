package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.dto.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.repository.CateringForLocationRepository;
import pjatk.socialeventorganizer.social_event_support.invite.mapper.CateringForEventLocationInfoMapper;
import pjatk.socialeventorganizer.social_event_support.invite.response.CateringPlaceInfoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CateringForChosenEventLocationService {

    private final CateringForLocationRepository cateringForLocationRepository;

    private final CateringForEventLocationInfoMapper cateringForEventLocationInfoMapper;

    public List<CateringPlaceInfoResponse> getCateringForLocationInfoByOrganizedEventIdAndLocationId(long orgEventId, long locationId) {
        final Optional<List<CateringForChosenEventLocation>> optionalCateringList =
                cateringForLocationRepository.findCateringForChosenEventLocationByOrganizedEventId(orgEventId, locationId);

        if (!optionalCateringList.isPresent()) {
            return new ArrayList<>();
        }
        final List<CateringForChosenEventLocation> cateringList = optionalCateringList.get();
        return cateringList.stream()
                .map(cateringForEventLocationInfoMapper::mapToResponse)
                .collect(Collectors.toList());

    }


}
