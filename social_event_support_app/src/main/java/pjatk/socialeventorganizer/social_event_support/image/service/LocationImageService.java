package pjatk.socialeventorganizer.social_event_support.image.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.validator.ImageValidator;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.image.mapper.ImageMapper;
import pjatk.socialeventorganizer.social_event_support.image.model.LocationImage;
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto;
import pjatk.socialeventorganizer.social_event_support.image.repository.LocationImageRepository;
import pjatk.socialeventorganizer.social_event_support.image.util.ImageUtil;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class LocationImageService {

    private static final int MAX_IMAGE_COUNT = 8;

    private final LocationImageRepository locationImageRepository;

    private final LocationService locationService;

    @Transactional(rollbackOn = Exception.class)
    public List<LocationImage> saveMultiple(long locationId, List<ImageDto> dtos) {
        if (dtos.size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only upload " + MAX_IMAGE_COUNT + " images at a time");
        }

        final Location location = locationService.getWithImages(locationId);
        if (location.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only have no more than " + MAX_IMAGE_COUNT + " images");
        }
        final List<LocationImage> result = new ArrayList<>();

        for (ImageDto dto : new HashSet<>(dtos)) {
            final byte[] data = ImageUtil.fromPathToByteArray(dto.getPath());

            final LocationImage locationImage = (LocationImage) ImageMapper.fromDto(dto);
            locationImage.setLocation(location);
            locationImage.setImage(data);

            create(locationImage);
            result.add(locationImage);
        }

        return result;
    }

    public LocationImage create(long locationId, ImageDto dto) {
        ImageValidator.validateFileExtension(dto.getPath());

        if (dto.isMain()) {
            final boolean exists = mainExists(locationId);
            dto.setMain(!exists);
        }
            final Location location = locationService.getWithImages(locationId);
        if (location.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only have no more than " + MAX_IMAGE_COUNT + " images");
        }
        final byte[] data = ImageUtil.fromPathToByteArray(dto.getPath());

        final LocationImage locationImage = (LocationImage) ImageMapper.fromDto(dto);
        locationImage.setLocation(location);
        locationImage.setImage(data);

        create(locationImage);

        return locationImage;
    }

    public List<LocationImage> findByLocationId(long locationId) {
        return locationImageRepository.findAllByLocation_Id(locationId);
    }

    private LocationImage get(long imageId) {
        return locationImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));
    }

    public int count(long locationId) {
        return locationImageRepository.countAll(locationId);
    }

    @Transactional(rollbackOn = Exception.class)
    public void setNewMain(long oldId, long newId) {
        final LocationImage oldMain = get(oldId);
        final LocationImage newMain = get(newId);

        oldMain.setMain(false);
        newMain.setMain(true);

        create(oldMain);
        create(newMain);
        locationImageRepository.flush();
    }

    public void deleteById(long oldId, Long newId) {
        final LocationImage toDelete = get(oldId);
        if (toDelete.isMain() && newId != null) {
            setNewMain(oldId, newId);
        }
        locationImageRepository.deleteById(oldId);
    }

    public Optional<LocationImage> getMain(long serviceId) {
        return locationImageRepository.getMain(serviceId);

    }

    private void create(LocationImage image) {
        locationImageRepository.save(image);
    }

    private boolean mainExists(long serviceId) {
        return getMain(serviceId).isPresent();
    }
}
