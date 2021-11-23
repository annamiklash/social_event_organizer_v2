package pjatk.socialeventorganizer.social_event_support.customer.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.address.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.customer.guest.mapper.GuestMapper;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.user.mapper.UserMapper;

import java.util.stream.Collectors;

@UtilityClass
public class CustomerMapper {

    public CustomerDto toDto(Customer customer) {
        return CustomerDto.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(String.valueOf(customer.getPhoneNumber()))
                .birthdate(customer.getBirthdate().toString())
//                .user(UserMapper.toDto(customer.getUser()))
                .build();
    }

    public CustomerDto toDtoWithGuests(Customer customer) {
        return CustomerDto.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(String.valueOf(customer.getPhoneNumber()))
                .birthdate(customer.getBirthdate().toString())
                .user(UserMapper.toDto(customer.getUser()))
                .guests(customer.getGuests().stream().map(GuestMapper::toDto).collect(Collectors.toSet()))
                .build();
    }


    public CustomerDto toDtoWithProblems(Customer customer) {
        return CustomerDto.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(String.valueOf(customer.getPhoneNumber()))
                .birthdate(customer.getBirthdate().toString())
                .user(UserMapper.toDtoWithProblems(customer.getUser()))
                .build();
    }

    public CustomerDto toDtoWithDetail(Customer customer) {
        return CustomerDto.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(String.valueOf(customer.getPhoneNumber()))
                .birthdate(customer.getBirthdate().toString())
                .user(UserMapper.toDtoWithProblems(customer.getUser()))
                .guests(customer.getGuests().stream()
                        .map(GuestMapper::toDto)
                        .collect(Collectors.toSet()))
                .events(customer.getEvents().stream()
                        .map(OrganizedEventMapper::toDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    public CustomerDto toDtoWithEvents(Customer customer) {
        return CustomerDto.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(String.valueOf(customer.getPhoneNumber()))
                .birthdate(customer.getBirthdate().toString())
                .user(UserMapper.toDtoWithProblems(customer.getUser()))
//                .guests(customer.getGuests().stream().map(GuestMapper::toDto).collect(Collectors.toSet()))
                .events(customer.getEvents().stream().map(OrganizedEventMapper::toDto).collect(Collectors.toSet()))
                .build();
    }

    public Customer fromDto(CustomerDto customer) {
        return Customer.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .birthdate(DateTimeUtil.fromStringToFormattedDate(customer.getBirthdate()))
                .phoneNumber(Converter.convertPhoneNumberString(customer.getPhoneNumber()))
                .address(AddressMapper.fromDto(customer.getAddress()))
                .build();
    }
}
