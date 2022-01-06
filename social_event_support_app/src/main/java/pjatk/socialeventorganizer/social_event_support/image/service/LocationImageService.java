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
import pjatk.socialeventorganizer.social_event_support.image.model.LocationImage;
import pjatk.socialeventorganizer.social_event_support.image.model.dto.ImageDto;
import pjatk.socialeventorganizer.social_event_support.image.repository.LocationImageRepository;
import pjatk.socialeventorganizer.social_event_support.image.util.ImageUtil;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;

import javax.transaction.Transactional;
import java.io.IOException;
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

            final LocationImage locationImage = ImageMapper.fromDtoToLocationImage(dto);
            locationImage.setLocation(location);
            locationImage.setImage(data);

            locationImageRepository.save(locationImage);
            result.add(locationImage);
        }

        return result;
    }

    public LocationImage create(long locationId, ImageDto dto) {
        ImageValidator.validateFileExtension(dto.getPath());

        final boolean exists = mainExists(locationId);
        dto.setMain(!exists);

        final Location location = locationService.getWithImages(locationId);
        if (location.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only have no more than " + MAX_IMAGE_COUNT + " images");
        }
        final byte[] data = ImageUtil.fromPathToByteArray(dto.getPath());

        final LocationImage locationImage = ImageMapper.fromDtoToLocationImage(dto);
        locationImage.setLocation(location);
        locationImage.setImage(data);

        locationImageRepository.save(locationImage);

        return locationImage;
    }

    public List<LocationImage> findByLocationId(long locationId) {
        return locationImageRepository.findAllByLocation_Id(locationId);
    }

    public int count(long locationId) {
        return locationImageRepository.countAll(locationId);
    }

    @Transactional(rollbackOn = Exception.class)
    public void setNewMain(long locationId, long newId) {
        final Optional<LocationImage> optionalImage = getMain(locationId);
        if (optionalImage.isPresent()) {
            final LocationImage locationImage = optionalImage.get();
            locationImage.setMain(false);

            locationImageRepository.save(locationImage);
        }
        final LocationImage newMain = get(newId);
        newMain.setMain(true);

        locationImageRepository.save(newMain);
        locationImageRepository.flush();
    }

    public void deleteById(long locationId, Long imageId) {
        final LocationImage imageToDelete = locationImageRepository.findByIdAndLocation_Id(imageId, locationId)
                .orElseThrow(() -> new NotFoundException("Location with id " + locationId + " does not have image with id " + imageId));

        locationImageRepository.delete(imageToDelete);
    }

    public Optional<LocationImage> getMain(long serviceId) {
        return locationImageRepository.getMain(serviceId);

    }

    private LocationImage get(long imageId) {
        return locationImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));
    }


    private boolean mainExists(long serviceId) {
        return getMain(serviceId).isPresent();
    }

    @SneakyThrows(IOException.class)
    public void upload(long locationId, MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new ActionNotAllowedException("Cannot upload from empty path");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        final Location location = locationService.getWithImages(locationId);
        final boolean exists = mainExists(locationId);

        final LocationImage locationImage = LocationImage.builder().location(location)
                .fileName(fileName)
                .isMain(!exists)
                .image(file.getBytes())
                .build();
        locationImageRepository.save(locationImage);

    }
}
