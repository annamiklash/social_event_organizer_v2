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
import pjatk.socialeventorganizer.social_event_support.image.model.CateringImage;
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto;
import pjatk.socialeventorganizer.social_event_support.image.service.CateringImageService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/images/catering")
public class CateringImageController {

    private final CateringImageService cateringImageService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<ImageDto>> list(@RequestParam long cateringId) {
        final List<CateringImage> list = cateringImageService.findByCateringId(cateringId);
        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(ImageMapper::toDto)
                        .collect(Collectors.toList()))
        );
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/main",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageDto> main(@RequestParam long cateringId) {
        final Optional<CateringImage> main = cateringImageService.getMain(cateringId);
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
        final CateringImage cateringImage = cateringImageService.create(cateringId, dto);
        return ResponseEntity.ok(ImageMapper.toDto(cateringImage));
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "multiple",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<ImageDto>> saveMultiple(@RequestParam long cateringId, @Valid @RequestBody ImageDto[] dtos) {
        final List<CateringImage> list = cateringImageService.saveMultiple(cateringId, Arrays.asList(dtos));
        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(ImageMapper::toDto)
                        .collect(Collectors.toList()))
        );
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT)
    public ResponseEntity<Void> changeMain(@RequestParam long cateringId, @RequestParam long imageId) {
        cateringImageService.setNewMain(cateringId, imageId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam long cateringId, @RequestParam long imageId) {
        cateringImageService.deleteById(cateringId, imageId);
        return ResponseEntity.ok().build();

    }

}
