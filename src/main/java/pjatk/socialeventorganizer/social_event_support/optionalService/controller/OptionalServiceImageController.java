package pjatk.socialeventorganizer.social_event_support.optionalService.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.image.model.response.ImageResponse;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.dto.OptionalServiceImage;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.request.OptionalServiceImageRequest;
import pjatk.socialeventorganizer.social_event_support.optionalService.service.OptionalServiceImageService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/optional_service_image")
public class OptionalServiceImageController {

    private final OptionalServiceImageService service;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageResponse> addOptionalService(@Valid @RequestBody OptionalServiceImageRequest request) {
        final ImageResponse response = service.addImagesToOptionalService(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/optional_service/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OptionalServiceImage>> getImagesByOptionalServiceId(@PathVariable Integer id) {
        try {
            final List<OptionalServiceImage> response = service.findByOptionalServiceId(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
}
