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
import pjatk.socialeventorganizer.social_event_support.image.mapper.ImageMapper;
import pjatk.socialeventorganizer.social_event_support.image.model.OptionalServiceImage;
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto;
import pjatk.socialeventorganizer.social_event_support.image.repository.OptionalServiceImageRepository;
import pjatk.socialeventorganizer.social_event_support.image.util.ImageUtil;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceImageService {

    private static final int MAX_IMAGE_COUNT = 8;

    private final OptionalServiceImageRepository optionalServiceImageRepository;

    private final OptionalServiceService optionalServiceService;

    @Transactional(rollbackOn = Exception.class)
    public List<OptionalServiceImage> saveMultiple(long serviceId, List<ImageDto> dtos) {
        if (dtos.size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only upload " + MAX_IMAGE_COUNT + " images at a time");
        }

        final OptionalService service = optionalServiceService.getWithImages(serviceId);
        if (service.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only have no more than " + MAX_IMAGE_COUNT + " images");
        }
        final List<OptionalServiceImage> result = new ArrayList<>();

        for (ImageDto dto : new HashSet<>(dtos)) {
            final byte[] data = ImageUtil.fromPathToByteArray(dto.getPath());

            final OptionalServiceImage image = ImageMapper.fromDtoToServiceImage(dto);
            image.setService(service);
            image.setImage(data);

            optionalServiceImageRepository.save(image);
            result.add(image);
        }

        return result;
    }

    public OptionalServiceImage create(long serviceId, ImageDto dto) {
        ImageValidator.validateFileExtension(dto.getPath());

        final boolean exists = mainExists(serviceId);
        dto.setMain(!exists);

        final OptionalService service = optionalServiceService.getWithImages(serviceId);
        if (service.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only have no more than " + MAX_IMAGE_COUNT + " images");
        }
        final byte[] data = ImageUtil.fromPathToByteArray(dto.getPath());

        final OptionalServiceImage image = ImageMapper.fromDtoToServiceImage(dto);
        image.setService(service);
        image.setImage(data);

        optionalServiceImageRepository.save(image);

        return image;
    }

    @SneakyThrows(IOException.class)
    public void upload(long serviceId, MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new ActionNotAllowedException("Cannot upload from empty path");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        ImageValidator.validateFileExtension(fileName);

        final OptionalService service = optionalServiceService.getWithImages(serviceId);
        if (service.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only have no more than " + MAX_IMAGE_COUNT + " images");
        }
        final boolean exists = mainExists(serviceId);

        final OptionalServiceImage locationImage = OptionalServiceImage.builder()
                .service(service)
                .fileName(fileName)
                .isMain(!exists)
                .image(file.getBytes())
                .build();

        optionalServiceImageRepository.save(locationImage);
    }

    @Transactional(rollbackOn = Exception.class)
    public void setNewMain(long cateringId, long newId) {
        final Optional<OptionalServiceImage> optionalImage = getMain(cateringId);
        if (optionalImage.isPresent()) {
            final OptionalServiceImage serviceImage = optionalImage.get();
            serviceImage.setMain(false);

            optionalServiceImageRepository.save(serviceImage);
        }
        final OptionalServiceImage newMain = get(newId);
        newMain.setMain(true);

        optionalServiceImageRepository.save(newMain);
        optionalServiceImageRepository.flush();
    }

    public int count(long serviceId) {
        return optionalServiceImageRepository.countAll(serviceId);
    }

    public List<OptionalServiceImage> findByServiceId(long serviceId) {
        return optionalServiceImageRepository.findAllByService_Id(serviceId);
    }

    public void deleteById(long serviceId, Long imageId) {
        final OptionalServiceImage imageToDelete = optionalServiceImageRepository.findByIdAndService_Id(imageId, serviceId)
                .orElseThrow(() -> new NotFoundException("Catering with id " + serviceId + " does not have image with id " + imageId));
        optionalServiceImageRepository.delete(imageToDelete);
    }

    public Optional<OptionalServiceImage> getMain(long serviceId) {
        return optionalServiceImageRepository.getMain(serviceId);
    }

    private OptionalServiceImage get(long imageId) {
        return optionalServiceImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));
    }

    private boolean mainExists(long serviceId) {
        return getMain(serviceId).isPresent();
    }
}
