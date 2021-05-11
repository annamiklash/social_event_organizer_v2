package pjatk.socialeventorganizer.social_event_support.location.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDescriptionItem;
import pjatk.socialeventorganizer.social_event_support.location.repository.LocationDescriptionItemRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LocationDescriptionItemService {

    LocationDescriptionItemRepository repository;

    public ImmutableList<LocationDescriptionItem> findAll() {
        final List<LocationDescriptionItem> itemsList =  repository.findAll();
        return ImmutableList.copyOf(itemsList);
    }

    public LocationDescriptionItem getById(String id) {
        return repository.getLocationDescriptionItemByName(id);
    }
}
