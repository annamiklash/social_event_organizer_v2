package pjatk.socialeventorganizer.social_event_support.location.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.location.model.LocationDescriptionItem;
import pjatk.socialeventorganizer.social_event_support.location.repository.LocationDescriptionItemRepository;

@Service
@AllArgsConstructor
@Slf4j
public class LocationDescriptionItemService {

   private final LocationDescriptionItemRepository repository;

    public LocationDescriptionItem getById(String id) {

        return repository.getLocationDescriptionItemByName(id);
    }
}
