package pjatk.socialeventorganizer.social_event_support.customer.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.appproblem.mapper.AppProblemMapper;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.customer.guest.mapper.GuestMapper;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.CustomerUserRegistrationDto;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.UserDto;

import java.util.stream.Collectors;

@UtilityClass
public class CustomerMapper {

    public CustomerDto toDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(String.valueOf(customer.getPhoneNumber()))
                .birthdate(customer.getBirthdate().toString())
                .user(UserDto.builder()
                        .id(customer.getId())
                        .email(customer.getEmail())
                        .type(customer.getType())
                        .createdAt(DateTimeUtil.fromLocalDateTimetoString(customer.getCreatedAt()))
                        .modifiedAt(DateTimeUtil.fromLocalDateTimetoString(customer.getModifiedAt()))
                        .build())
                .build();
    }

    public CustomerDto toDtoWithGuests(Customer customer) {
        return CustomerDto.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(String.valueOf(customer.getPhoneNumber()))
                .birthdate(customer.getBirthdate().toString())
                .user(UserDto.builder()
                        .id(customer.getId())
                        .email(customer.getEmail())
                        .type(customer.getType())
                        .createdAt(DateTimeUtil.fromLocalDateTimetoString(customer.getCreatedAt()))
                        .modifiedAt(DateTimeUtil.fromLocalDateTimetoString(customer.getModifiedAt()))
                        .build())
                .guests(customer.getGuests().stream()
                        .map(GuestMapper::toDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    public CustomerDto toDtoWithProblems(Customer customer) {
        final CustomerDto dto = toDto(customer);
        final UserDto user = dto.getUser();

        user.setAppProblems(customer.getAppProblems()
                .stream()
                .map(AppProblemMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public CustomerDto toDtoWithDetail(Customer customer) {
        final CustomerDto dto = toDtoWithProblems(customer);

        dto.setGuests(customer.getGuests().stream()
                .map(GuestMapper::toDto)
                .collect(Collectors.toSet()));
        dto.setEvents(customer.getEvents().stream()
                .map(OrganizedEventMapper::toDto)
                .collect(Collectors.toSet()));

        return dto;
    }


    public Customer fromCustomerRegistrationDto(CustomerUserRegistrationDto dto) {
        return Customer.builder()
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthdate(DateTimeUtil.fromStringToLocalDate(dto.getBirthdate()))
                .isActive(true)
                .type(dto.getType())
                .phoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()))
                .build();
    }

    public Customer fromDto(CustomerDto customer) {
        return Customer.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .birthdate(DateTimeUtil.fromStringToFormattedDate(customer.getBirthdate()))
                .phoneNumber(Converter.convertPhoneNumberString(customer.getPhoneNumber()))
                .build();
    }
}
