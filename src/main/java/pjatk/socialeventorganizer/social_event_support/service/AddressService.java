package pjatk.socialeventorganizer.social_event_support.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.model.exception.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.repository.AddressRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AddressService {

    AddressRepository repository;

    public Address findById(Long id) {
        final Optional<Address> optionalAddress = repository.findById(id);
        if (optionalAddress.isPresent()) {
            return optionalAddress.get();
        }
        throw new NotFoundException("Address with id " + id + " DOES NOT EXIST");
    }

    public boolean addressWithIdExists(Long id) {
        log.info("CHECKING IF ADDRESS WITH ID " + id + " EXISTS");
        return repository.existsById(id);
    }
}
