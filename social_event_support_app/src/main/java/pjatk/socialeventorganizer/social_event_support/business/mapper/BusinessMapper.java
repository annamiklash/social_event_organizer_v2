package pjatk.socialeventorganizer.social_event_support.business.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.address.mapper.AddressMapper;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringMapper;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.location.mapper.LocationMapper;
import pjatk.socialeventorganizer.social_event_support.user.mapper.UserMapper;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.BusinessUserRegistrationDto;

import java.util.stream.Collectors;


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

    public BusinessDto fromBusinessUserRegistrationDto(BusinessUserRegistrationDto dto) {
        return BusinessDto.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .businessName(dto.getBusinessName())
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .user(dto.getUser())
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
                .user(UserMapper.toDto(business.getUser()))
                .build();
    }

    public BusinessDto toDtoWithDetail(Business business) {
        final BusinessDto dto = toDto(business);
        dto.setLocations(business.getLocations().stream().map(LocationMapper::toDto).collect(Collectors.toSet()));
        dto.setCaterings(business.getCaterings().stream().map(CateringMapper::toDto).collect(Collectors.toSet()));

        return dto;
    }
}
