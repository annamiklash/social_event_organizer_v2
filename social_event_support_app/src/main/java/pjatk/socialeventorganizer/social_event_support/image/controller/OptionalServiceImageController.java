package pjatk.socialeventorganizer.social_event_support.image.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.image.mapper.ImageMapper;
import pjatk.socialeventorganizer.social_event_support.image.model.OptionalServiceImage;
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto;
import pjatk.socialeventorganizer.social_event_support.image.service.OptionalServiceImageService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/images/service")
public class OptionalServiceImageController {

    private final OptionalServiceImageService optionalServiceImageService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<ImageDto>> list(@RequestParam long serviceId) {
        final List<OptionalServiceImage> list = optionalServiceImageService.findByServiceId(serviceId);
        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(ImageMapper::toDto)
                        .collect(Collectors.toList()))

        );
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageDto> main(@RequestParam long serviceId) {
        final Optional<OptionalServiceImage> main = optionalServiceImageService.getMain(serviceId);
        if (main.isEmpty()) {
            throw new NotFoundException("No main image");
        }
        return ResponseEntity.ok(ImageMapper.toDto(main.get()));
    }



    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageDto> save(@RequestParam long cateringId, @Valid @RequestBody ImageDto dto) {
        final OptionalServiceImage serviceImage = optionalServiceImageService.create(cateringId, dto);
        return ResponseEntity.ok(ImageMapper.toDto(serviceImage));
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "multiple",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<ImageDto>> saveMultiple(@RequestParam long cateringId, @Valid @RequestBody ImageDto[] dtos) {
        final List<OptionalServiceImage> list = optionalServiceImageService.saveMultiple(cateringId, Arrays.asList(dtos));
        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(ImageMapper::toDto)
                        .collect(Collectors.toList()))

        );
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT)
    public ResponseEntity<Void> changeMain(@RequestParam long oldId, @RequestParam long newId) {
        optionalServiceImageService.setNewMain(oldId, newId);
        return ResponseEntity.ok().build();

    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam long oldId, @RequestParam long newId) {
        optionalServiceImageService.deleteById(oldId, newId);
        return ResponseEntity.ok().build();

    }

}
