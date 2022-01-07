package pjatk.socialeventorganizer.social_event_support.image.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.validator.ImageValidator;
import pjatk.socialeventorganizer.social_event_support.exceptions.ActionNotAllowedException;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.image.mapper.ImageMapper;
import pjatk.socialeventorganizer.social_event_support.image.model.CateringImage;
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto;
import pjatk.socialeventorganizer.social_event_support.image.repository.CateringImageRepository;
import pjatk.socialeventorganizer.social_event_support.image.util.ImageUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CateringImageService {

    private static final int MAX_IMAGE_COUNT = 8;

    private final CateringImageRepository cateringImageRepository;

    private final CateringService cateringService;

    @Transactional(rollbackOn = Exception.class)
    public List<CateringImage> saveMultiple(long locationId, List<ImageDto> dtos) {
        if (dtos.size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only upload " + MAX_IMAGE_COUNT + " images at a time");
        }

        final Catering catering = cateringService.getWithImages(locationId);
        if (catering.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only have no more than " + MAX_IMAGE_COUNT + " images");
        }
        final List<CateringImage> result = new ArrayList<>();

        for (ImageDto dto : new HashSet<>(dtos)) {
            final byte[] data = ImageUtil.fromPathToByteArray(dto.getPath());

            final CateringImage cateringImage = ImageMapper.fromDtoToCateringImage(dto);
            cateringImage.setCatering(catering);
            cateringImage.setImage(data);

            cateringImageRepository.save(cateringImage);
            result.add(cateringImage);
        }

        return result;
    }

    @SneakyThrows(IOException.class)
    public void upload(long cateringId, MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new ActionNotAllowedException("Cannot upload from empty path");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        ImageValidator.validateFileExtension(fileName);

        final Catering catering = cateringService.getWithImages(cateringId);
        if (catering.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only have no more than " + MAX_IMAGE_COUNT + " images");
        }
        final boolean exists = mainExists(cateringId);

        final CateringImage cateringImage = CateringImage.builder()
                .catering(catering)
                .fileName(fileName)
                .isMain(!exists)
                .image(file.getBytes())
                .build();

        cateringImageRepository.save(cateringImage);
    }

    public CateringImage create(long cateringId, ImageDto dto) {
        ImageValidator.validateFileExtension(dto.getPath());

        final boolean exists = mainExists(cateringId);
        dto.setMain(!exists);

        final Catering catering = cateringService.getWithImages(cateringId);
        if (catering.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only have no more than " + MAX_IMAGE_COUNT + " images");
        }

        final byte[] data = ImageUtil.fromPathToByteArray(dto.getPath());
        final CateringImage image = ImageMapper.fromDtoToCateringImage(dto);
        image.setCatering(catering);
        image.setImage(data);

        cateringImageRepository.save(image);

        return image;
    }

    @Transactional(rollbackOn = Exception.class)
    public void setNewMain(long cateringId, long newId) {
        final Optional<CateringImage> optionalImage = getMain(cateringId);
        if (optionalImage.isPresent()) {
            final CateringImage cateringImage = optionalImage.get();
            cateringImage.setMain(false);

            cateringImageRepository.save(cateringImage);
        }
        final CateringImage newMain = get(newId);
        newMain.setMain(true);

        cateringImageRepository.save(newMain);
        cateringImageRepository.flush();
    }

    public List<CateringImage> findByCateringId(long cateringId) {
        return cateringImageRepository.findAllByCatering_Id(cateringId);
    }

    public int count(long cateringId) {
        return cateringImageRepository.countAll(cateringId);
    }

    public void deleteById(long cateringId, Long imageId) {
        final CateringImage image = cateringImageRepository.getCateringImageByIdAndCatering_Id(imageId, cateringId)
                .orElseThrow(() -> new NotFoundException("Catering with id " + cateringId + " does not have image with id " + imageId));
        cateringImageRepository.delete(image);
    }

    public Optional<CateringImage> getMain(long serviceId) {
        return cateringImageRepository.getMain(serviceId);
    }

    private CateringImage get(long imageId) {
        return cateringImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));
    }

    private boolean mainExists(long serviceId) {
        return getMain(serviceId).isPresent();
    }


}
