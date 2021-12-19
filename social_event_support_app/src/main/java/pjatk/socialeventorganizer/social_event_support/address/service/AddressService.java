package pjatk.socialeventorganizer.social_event_support.address.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto;
import pjatk.socialeventorganizer.social_event_support.address.repository.AddressRepository;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.common.util.TimestampUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final TimestampUtil timestampUtil;

    public ImmutableList<Address> list(CustomPage customPagination) {
        Pageable paging = PageRequest.of(customPagination.getPageNo(), customPagination.getPageSize(), Sort.by(customPagination.getSortBy()).descending());
        final Page<Address> page = addressRepository.findAll(paging);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    public Address get(long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address with id " + id + " DOES NOT EXIST"));
    }

    public Address getByUserId(long id) {
        return addressRepository.findByUserId(id)
                .orElseThrow(() -> new NotFoundException("Address with user id " + id + " DOES NOT EXIST"));
    }

    public Address getByLocationId(long id) {
        return addressRepository.findByLocationId(id)
                .orElseThrow(() -> new NotFoundException("Address with location id " + id + " DOES NOT EXIST"));
    }

    public Address getByCateringId(long id) {
        return addressRepository.findByCateringId(id)
                .orElseThrow(() -> new NotFoundException("Address with catering id " + id + " DOES NOT EXIST"));
    }

    public Address getByServiceId(long id) {
        return addressRepository.findByServiceId(id)
                .orElseThrow(() -> new NotFoundException("Address with service id " + id + " DOES NOT EXIST"));
    }

    public boolean addressWithIdExists(Long id) {
        return addressRepository.existsById(id);
    }

    public Address create(AddressDto dto) {
        final Address address = AddressMapper.fromDto(dto);

        address.setCreatedAt(timestampUtil.now());
        address.setModifiedAt(timestampUtil.now());

        save(address);
        return address;
    }

    public Address edit(long id, AddressDto dto) {
        if (!addressWithIdExists(id)) {
            throw new IllegalArgumentException("Address with ID " + id + " does not exist");
        }

        final Address address = get(id);

        //Cant change city or country -> delete and create new catering
        if (!dto.getCity().equals(address.getCity()) || !dto.getCountry().equals(address.getCountry())) {
            throw new IllegalArgumentException("In order to change city or country, you have to create new catering in a given city");
        }
        address.setStreetName(dto.getStreetName());
        address.setStreetNumber(dto.getStreetNumber());
        address.setZipCode(dto.getZipCode());
        address.setModifiedAt(timestampUtil.now());

        save(address);

        return address;
    }

    public void delete(long id) {
        final Address address = get(id);

        address.setModifiedAt(timestampUtil.now());
        address.setDeletedAt(timestampUtil.now());

        addressRepository.save(address);
    }

    public void delete(Address address) {

        address.setModifiedAt(timestampUtil.now());
        address.setDeletedAt(timestampUtil.now());

        addressRepository.save(address);
    }


    public void save(Address address) {
        addressRepository.save(address);
    }

    public Long count() {
        return addressRepository.count();
    }


}
