package pjatk.socialeventorganizer.social_event_support.business.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.address.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringMapper;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.location.mapper.LocationMapper;
import pjatk.socialeventorganizer.social_event_support.optional_service.mapper.OptionalServiceMapper;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.BusinessUserRegistrationDto;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.UserDto;

import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum.NOT_VERIFIED;


@UtilityClass
public class BusinessMapper {

    public Business fromDto(BusinessDto dto) {
        return Business.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .businessName(dto.getBusinessName())
                .phoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()))
                .build();
    }

    public Business fromBusinessUserRegistrationDto(BusinessUserRegistrationDto dto) {
        return Business.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .businessName(dto.getBusinessName())
                .phoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()))
                .type(dto.getType())
                .businessName(dto.getBusinessName())
                .isActive(true)
                .verificationStatus(NOT_VERIFIED.name())
                .build();
    }


    public BusinessDto toDto(Business business) {
        return BusinessDto.builder()
                .id(business.getId())
                .firstName(business.getFirstName())
                .lastName(business.getLastName())
                .businessName(business.getBusinessName())
                .verificationStatus(business.getVerificationStatus())
                .phoneNumber(String.valueOf(business.getPhoneNumber()))
                .address(AddressMapper.toDto(business.getAddress()))
                .build();
    }

    public BusinessDto toDtoWithUser(Business business) {
        return BusinessDto.builder()
                .id(business.getId())
                .firstName(business.getFirstName())
                .lastName(business.getLastName())
                .businessName(business.getBusinessName())
                .verificationStatus(business.getVerificationStatus())
                .phoneNumber(String.valueOf(business.getPhoneNumber()))
                .address(AddressMapper.toDto(business.getAddress()))
                .user(UserDto.builder()
                        .id(business.getId())
                        .email(business.getEmail())
                        .type(business.getType())
                        .createdAt(DateTimeUtil.fromLocalDateTimetoString(business.getCreatedAt()))
                        .modifiedAt(DateTimeUtil.fromLocalDateTimetoString(business.getModifiedAt()))
                        .build())
                .build();
    }

    public BusinessDto toDtoWithDetail(Business business) {
        final BusinessDto dto = toDto(business);

        dto.setLocations(business.getLocations().stream()
                .map(LocationMapper::toDto)
                .collect(Collectors.toSet()));

        dto.setCaterings(business.getCaterings().stream()
                .map(CateringMapper::toDto)
                .collect(Collectors.toSet()));

        dto.setServices(business.getServices().stream()
                .map(OptionalServiceMapper::toDto)
                .collect(Collectors.toSet()));

        return dto;
    }
}
