package pjatk.socialeventorganizer.social_event_support.image.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.validator.ImageValidator;
import pjatk.socialeventorganizer.social_event_support.exceptions.ActionNotAllowedException;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.image.model.OptionalServiceImage;
import pjatk.socialeventorganizer.social_event_support.image.repository.OptionalServiceImageRepository;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceImageService {

    private static final int MAX_IMAGE_COUNT = 8;

    private final OptionalServiceImageRepository optionalServiceImageRepository;

    private final OptionalServiceService optionalServiceService;


    @SneakyThrows(IOException.class)
    public void upload(long serviceId, MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new ActionNotAllowedException("Cannot upload from empty path");
        }
        final OptionalService service = optionalServiceService.getWithImages(serviceId);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        ImageValidator.validateFileExtension(fileName);

        if (service.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only have no more than " + MAX_IMAGE_COUNT + " images");
        }

        final OptionalServiceImage locationImage = OptionalServiceImage.builder()
                .service(service)
                .fileName(fileName)
                .image(file.getBytes())
                .build();

        optionalServiceImageRepository.save(locationImage);
    }


    public int count(long serviceId) {
        return optionalServiceImageRepository.countAll(serviceId);
    }

    public List<OptionalServiceImage> findByServiceId(long serviceId) {
        return optionalServiceImageRepository.findAllByService_Id(serviceId);
    }

    public void deleteById( Long imageId) {
        final OptionalServiceImage imageToDelete = optionalServiceImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Service image with id " + imageId + " does not exist"));
        optionalServiceImageRepository.delete(imageToDelete);
    }

}
