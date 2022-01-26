package pjatk.socialeventorganizer.social_event_support.customer.avatar.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.CustomerAvatar;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.repository.CustomerAvatarRepository;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
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
//        if (file.getOriginalFilename() == null) {
//            throw new ActionNotAllowedException("Cannot upload from empty path");
//        }
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        ImageValidator.validateFileExtension(fileName);
//
//        final Customer customer = customerRepository.getByIdWithAvatar(customerId)
//                .orElseThrow(() -> new NotFoundException("Customer does not exist"));
//
//        final Optional<CustomerAvatar> optionalAvatar = customerAvatarRepository.findCustomerAvatarByCustomer_Id(customerId);
//
//        if (optionalAvatar.isPresent()) {
//
//            final CustomerAvatar customerAvatar = optionalAvatar.get();
//            customerAvatar.setImage(file.getBytes());
//            customerAvatarRepository.save(customerAvatar);
//            return;
//
//        } else {
//            final CustomerAvatar avatar = CustomerAvatar.builder()
//                    .fileName(fileName)
//                    .image(file.getBytes())
//                    .build();
//
//            customerAvatarRepository.save(avatar);
//        }
    }

    public void deleteById(long id) {
        final Customer customer = customerRepository.getByIdWithAvatar(id)
                .orElseThrow(() -> new NotFoundException("Customer does not exist"));

        customerAvatarRepository.deleteById(customer.getAvatar().getId());
    }

    public void save(CustomerAvatar customerAvatar) {
        customerAvatarRepository.save(customerAvatar);
    }

//    public Optional<CustomerAvatar> findCustomerAvatarByCustomer_Id(long customerId) {
//        return customerAvatarRepository.findCustomerAvatarByCustomer_Id(customerId);
//    }

    public void delete(CustomerAvatar customerAvatar) {
        customerAvatarRepository.delete(customerAvatar);
    }
}
