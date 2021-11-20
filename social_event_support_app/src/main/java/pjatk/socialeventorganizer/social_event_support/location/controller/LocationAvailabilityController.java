package pjatk.socialeventorganizer.social_event_support.location.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.location.availability.mapper.LocationAvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.location.availability.model.LocationAvailability;
import pjatk.socialeventorganizer.social_event_support.location.availability.model.dto.LocationAvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.location.availability.service.LocationAvailabilityService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/availability")
public class LocationAvailabilityController {


    private final LocationAvailabilityService locationAvailabilityService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "location",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationAvailabilityDto>> list(@RequestParam long id, @RequestParam String date) {

        final List<LocationAvailability> availabilities = locationAvailabilityService.findAllByLocationIdAndDate(id, date);

        return ResponseEntity.ok(
                ImmutableList.copyOf(availabilities.stream()
                        .map(LocationAvailabilityMapper::toDto)
                        .collect(Collectors.toList())));
    }
}
