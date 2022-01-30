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
import pjatk.socialeventorganizer.social_event_support.image.model.CateringImage;
import pjatk.socialeventorganizer.social_event_support.image.repository.CateringImageRepository;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CateringImageService {

    private static final int MAX_IMAGE_COUNT = 8;

    private final CateringImageRepository cateringImageRepository;

    private final CateringService cateringService;

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

        final CateringImage cateringImage = CateringImage.builder()
                .catering(catering)
                .fileName(fileName)
                .image(file.getBytes())
                .build();

        cateringImageRepository.save(cateringImage);
    }


    public List<CateringImage> findByCateringId(long cateringId) {
        return cateringImageRepository.findAllByCatering_Id(cateringId);
    }

    public void deleteById(long imageId) {
        final CateringImage image = cateringImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Catering image with id" + imageId + " does not exist"));
        cateringImageRepository.delete(image);
    }

}
