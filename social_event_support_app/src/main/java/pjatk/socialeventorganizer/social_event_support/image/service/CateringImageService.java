package pjatk.socialeventorganizer.social_event_support.image.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.validator.ImageValidator;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.image.mapper.ImageMapper;
import pjatk.socialeventorganizer.social_event_support.image.model.CateringImage;
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto;
import pjatk.socialeventorganizer.social_event_support.image.repository.CateringImageRepository;
import pjatk.socialeventorganizer.social_event_support.image.util.ImageUtil;

import javax.transaction.Transactional;
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

            final CateringImage cateringImage = (CateringImage) ImageMapper.fromDto(dto);
            cateringImage.setCatering(catering);
            cateringImage.setImage(data);

            save(cateringImage);
            result.add(cateringImage);
        }

        return result;
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
        final CateringImage image = (CateringImage) ImageMapper.fromDto(dto);
        image.setCatering(catering);
        image.setImage(data);

        save(image);

        return image;
    }

    @Transactional(rollbackOn = Exception.class)
    public void setNewMain(long cateringId, long newId) {
        final Optional<CateringImage> optionalImage = getMain(cateringId);
        if (optionalImage.isPresent()) {
            final CateringImage cateringImage = optionalImage.get();
            cateringImage.setMain(false);

            save(cateringImage);
        }
        final CateringImage newMain = get(newId);
        newMain.setMain(true);

        save(newMain);
        cateringImageRepository.flush();
    }

    public List<CateringImage> findByCateringId(long cateringId) {
        return cateringImageRepository.findAllByCatering_Id(cateringId);
    }

    public int count(long cateringId) {
        return cateringImageRepository.countAll(cateringId);
    }

    public void deleteById(long locationId, Long toDelete) {
        List<CateringImage> list = findByCateringId(locationId);
        if (list.size() == 1) {
            cateringImageRepository.deleteById(toDelete);
            return;
        }
        if (list.size() == 0){
            return;
        }
        final CateringImage imageToDelete = get(toDelete);
        if (imageToDelete.isMain()) {
            final Optional<CateringImage> optionalMain = getMain(locationId);
            if (optionalMain.isEmpty() || optionalMain.get().getId() == toDelete) {
                final CateringImage cateringImage = list.stream()
                        .filter(image -> !image.isMain() && image.getId() != toDelete)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("You should have at least 2 images to delete one"));
                cateringImage.setMain(true);
                save(cateringImage);
            }
        }
        cateringImageRepository.delete(imageToDelete);
    }

    public Optional<CateringImage> getMain(long serviceId) {
        return cateringImageRepository.getMain(serviceId);
    }

    private void save(CateringImage image) {
        cateringImageRepository.save(image);
    }

    private CateringImage get(long imageId) {
        return cateringImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));
    }

    private boolean mainExists(long serviceId) {
        return getMain(serviceId).isPresent();
    }

}
