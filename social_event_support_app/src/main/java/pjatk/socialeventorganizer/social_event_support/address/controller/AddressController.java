package pjatk.socialeventorganizer.social_event_support.address.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.address.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;

import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/address")
public class AddressController {

    private final AddressService addressService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AddressDto>> list(@RequestParam(defaultValue = "0") Integer firstResult,
                                                          @RequestParam(defaultValue = "50") Integer maxResult,
                                                          @RequestParam(defaultValue = "id") String sort,
                                                          @RequestParam(defaultValue = "desc") String order) {

        final ImmutableList<Address> addressList = addressService.list(new CustomPage(maxResult, firstResult, sort, order));

        return ResponseEntity.ok(ImmutableList.copyOf(addressList.stream().map(AddressMapper::toDto).collect(Collectors.toList())));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            params = {"id"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressDto> get(@RequestParam long id) {
        log.info("GET " + id);

        final Address address = addressService.get(id);

        return ResponseEntity.ok(AddressMapper.toDto(address));
    }

}
