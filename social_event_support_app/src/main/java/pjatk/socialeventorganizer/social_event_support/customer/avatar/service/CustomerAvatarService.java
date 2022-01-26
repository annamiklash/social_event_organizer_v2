package pjatk.socialeventorganizer.social_event_support.customer.avatar.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.CustomerAvatar;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.repository.CustomerAvatarRepository;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.validator.ImageValidator;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.ActionNotAllowedException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerAvatarService {

    private final CustomerAvatarRepository customerAvatarRepository;

    private final CustomerRepository customerRepository;

    @SneakyThrows(IOException.class)
    public void upload(long customerId, MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new ActionNotAllowedException("Cannot upload from empty path");
        }
        final Customer customer = customerRepository.getByIdWithAvatar(customerId)
                .orElseThrow(() -> new NotFoundException("Customer does not exist"));
        if (customer.getAvatar() != null) {
            delete(customer.getAvatar());
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        ImageValidator.validateFileExtension(fileName);

        final CustomerAvatar avatar = CustomerAvatar.builder()
                .customer(customer)
                .fileName(fileName)
                .image(file.getBytes())
                .build();

        customerAvatarRepository.save(avatar);
    }

    public void deleteById(long id) {
        final Customer customer = customerRepository.getByIdWithAvatar(id)
                .orElseThrow(() -> new NotFoundException("Customer does not exist"));

        customerAvatarRepository.deleteById(customer.getAvatar().getId());
    }

    private void delete(CustomerAvatar customerAvatar) {
        customerAvatarRepository.delete(customerAvatar);
    }
}
