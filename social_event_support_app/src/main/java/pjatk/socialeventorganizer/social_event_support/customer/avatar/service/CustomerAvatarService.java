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

@Service
@AllArgsConstructor
@Slf4j
public class CustomerAvatarService {

    private CustomerAvatarRepository customerAvatarRepository;

    @Transactional(rollbackOn = Exception.class)
    public CustomerAvatar create(CustomerAvatarDto dto) {

        ImageValidator.validateFileExtension(dto.getPath());

        final File file = new File(dto.getPath());
        final byte[] bFile = new byte[(int) file.length()];

        dto.setImage(bFile);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final CustomerAvatar customerAvatar = CustomerAvatarMapper.fromDto(dto);

        customerAvatarRepository.save(customerAvatar);

        return customerAvatar;
    }
}
