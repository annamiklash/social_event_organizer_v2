package pjatk.socialeventorganizer.social_event_support.customer.guest.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.common.mapper.PageableMapper;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.customer.guest.mapper.GuestMapper;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto;
import pjatk.socialeventorganizer.social_event_support.customer.guest.repository.GuestRepository;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GuestService {

    private GuestRepository guestRepository;

    private CustomerRepository customerRepository;

    public ImmutableList<Guest> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<Guest> page = guestRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    public List<Guest> listAllByCustomerId(long id) {
        final Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new NotFoundException("No customer with id " + id);
        }

        return guestRepository.getAllByCustomer_Id(id);
    }

    public List<Guest> getGuestsByIds(List<Long> guestIds) {
        return guestIds.stream()
                .map(id -> guestRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Guest get(long id) {
        final Optional<Guest> optionalGuest = guestRepository.findById(id);

        if (optionalGuest.isPresent()) {
            return optionalGuest.get();
        }
        throw new NotFoundException("No guest with id " + id);
    }

    public Guest create(long customerId, GuestDto dto) {
        final Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("No optionalCustomer with id " + customerId);
        }
        final Customer customer = optionalCustomer.get();
        final Guest guest = GuestMapper.fromDto(dto);

        guest.setCustomer(customer);
        guest.setCreatedAt(LocalDateTime.now());
        guest.setModifiedAt(LocalDateTime.now());
        save(guest);

        return guest;
    }

    public void delete(long customerId, long guestId) {
        final Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("No optionalCustomer with id " + customerId);
        }
        final Guest guest = get(guestId);
        guestRepository.delete(guest);
    }

    public void delete(Guest guest) {
        guestRepository.delete(guest);
    }


    private void save(Guest guest) {
        guestRepository.save(guest);
    }

}
