package pjatk.socialeventorganizer.social_event_support.customer.guest.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper;
import pjatk.socialeventorganizer.social_event_support.common.mapper.PageableMapper;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.customer.guest.mapper.GuestMapper;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto;
import pjatk.socialeventorganizer.social_event_support.customer.guest.repository.GuestRepository;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GuestService {

    private final GuestRepository guestRepository;

    private final CustomerRepository customerRepository;

    private final TimestampHelper timestampHelper;

    public ImmutableList<Guest> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<Guest> page = guestRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    public List<Guest> listAllByCustomerId(long id) {
        if (!customerRepository.existsById(id)) {
            throw new NotFoundException("No customer with id " + id);
        }

        return guestRepository.getAllByCustomer_Id(id);
    }

    public List<Guest> getGuestsByIds(List<Long> guestIds) {
        return guestIds.stream()
                .map(guestRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Guest get(long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No guest with id " + id));

    }

    public Guest create(long customerId, GuestDto dto) {
        final Optional<Customer> optionalCustomer = customerRepository.getByIdWithUser(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("No optionalCustomer with id " + customerId);
        }
        final Customer customer = optionalCustomer.get();
        final Guest guest = GuestMapper.fromDto(dto);

        guest.setCustomer(customer);
        guest.setCreatedAt(timestampHelper.now());
        guest.setModifiedAt(timestampHelper.now());
        guestRepository.save(guest);
        return guest;
    }

    public Guest edit(long guestId, GuestDto dto) {
        final Guest guest = get(guestId);

        guest.setFirstName(dto.getFirstName());
        guest.setLastName(dto.getLastName());
        guest.setEmail(dto.getEmail());
        guest.setModifiedAt(timestampHelper.now());

        guestRepository.save(guest);
        return guest;
    }

    public void delete(long customerId, long guestId) {
        final Optional<Customer> optionalCustomer = customerRepository.getByIdWithUser(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("No optionalCustomer with id " + customerId);
        }
        final Guest guest = get(guestId);
        guestRepository.delete(guest);
    }

    public void delete(Guest guest) {
        guestRepository.delete(guest);
    }

}
