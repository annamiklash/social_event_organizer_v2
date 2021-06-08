package pjatk.socialeventorganizer.social_event_support.optionalService.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.image.mapper.ImageMapper;
import pjatk.socialeventorganizer.social_event_support.image.model.request.ImageRequestDetails;
import pjatk.socialeventorganizer.social_event_support.image.model.response.ImageResponse;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.dto.OptionalServiceImage;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.request.OptionalServiceImageRequest;
import pjatk.socialeventorganizer.social_event_support.optionalService.repository.OptionalServiceImageRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceImageService {

    OptionalServiceImageRepository repository;

    ImageMapper mapper;

    public ImageResponse addImagesToOptionalService(OptionalServiceImageRequest request) {
        final Integer optionalServiceId = request.getOptionalServiceId();
        final List<ImageRequestDetails> details = request.getDetails();
        ImageResponse response = new ImageResponse();
        final List<Long> newImageIds = response.getNewImageIds();

        for (ImageRequestDetails detail : details) {
            final OptionalServiceImage optionalServiceImage = mapper.mapToOptionalServiceImageDTO(detail, optionalServiceId);
            final OptionalServiceImage save = repository.save(optionalServiceImage);
            newImageIds.add(save.getId());
        }

        return response;
    }

    public List<OptionalServiceImage> findByOptionalServiceId(int optionalServiceId) {
        final Optional<List<OptionalServiceImage>> allByOptionalServiceId = repository.findAllByOptionalServiceId(optionalServiceId);
        if (allByOptionalServiceId.isPresent() && !allByOptionalServiceId.get().isEmpty()) {
            return allByOptionalServiceId.get();
        }
        throw new NotFoundException("There are no images to show");

    }
}
