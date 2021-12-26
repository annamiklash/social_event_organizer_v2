package pjatk.socialeventorganizer.social_event_support.customer.avatar.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.mapper.CustomerAvatarMapper;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.CustomerAvatar;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.dto.CustomerAvatarDto;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.repository.CustomerAvatarRepository;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.validator.ImageValidator;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerAvatarService {

    private final CustomerAvatarRepository customerAvatarRepository;

    @Transactional(rollbackOn = Exception.class)
    public CustomerAvatar create(CustomerAvatarDto dto) {

        ImageValidator.validateFileExtension(dto.getPath());

        final File file = new File(dto.getPath());
        final byte[] bFile = new byte[(int) file.length()];

        dto.setImage(bFile);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            final int ignored = fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        final CustomerAvatar customerAvatar = CustomerAvatarMapper.fromDto(dto);

        customerAvatarRepository.save(customerAvatar);

        return customerAvatar;
    }

    public void delete(CustomerAvatar customerAvatar) {
        customerAvatarRepository.delete(customerAvatar);
    }
}
