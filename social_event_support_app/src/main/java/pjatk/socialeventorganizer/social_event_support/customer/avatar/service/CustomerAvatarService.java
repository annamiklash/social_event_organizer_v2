package pjatk.socialeventorganizer.social_event_support.customer.avatar.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.mapper.CustomerAvatarMapper;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.CustomerAvatar;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.dto.CustomerAvatarDto;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.repository.CustomerAvatarRepository;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.validator.ImageValidator;
import pjatk.socialeventorganizer.social_event_support.image.util.ImageUtil;

import javax.transaction.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerAvatarService {

    private final CustomerAvatarRepository customerAvatarRepository;

    @Transactional(rollbackOn = Exception.class)
    public CustomerAvatar create(CustomerAvatarDto dto) {
        ImageValidator.validateFileExtension(dto.getPath());

        final byte[] data = ImageUtil.fromPathToByteArray(dto.getPath());
        dto.setImage(data);

        final CustomerAvatar customerAvatar = CustomerAvatarMapper.fromDto(dto);

        customerAvatarRepository.save(customerAvatar);

        return customerAvatar;
    }

    public void delete(CustomerAvatar customerAvatar) {
        customerAvatarRepository.delete(customerAvatar);
    }
}
