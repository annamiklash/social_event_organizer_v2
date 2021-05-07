package pjatk.socialeventorganizer.social_event_support.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.model.dto.Business;
import pjatk.socialeventorganizer.social_event_support.model.enums.BusinessVerificationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.model.request.BusinessRequest;
import pjatk.socialeventorganizer.social_event_support.model.response.BusinessResponse;


@Component
public class BusinessMapper {

    public Business mapToDTO(BusinessRequest businessRequest) {

        return Business.builder()
                .firstName(businessRequest.getFirstName())
                .lastName(businessRequest.getLastName())
                .businessName(businessRequest.getBusinessName())
                .email(businessRequest.getEmail())
                .password(businessRequest.getPassword())
                .phoneNumber(Converter.convertPhoneNumberString(businessRequest.getPhoneNumber()))
                .verificationStatus(String.valueOf(BusinessVerificationStatusEnum.NOT_VERIFIED))
                .build();
    }

    public BusinessResponse mapToResponse(Business business) {
        return BusinessResponse.builder()
                .id(business.getId())
                .email(business.getEmail())
                .build();
    }
}
