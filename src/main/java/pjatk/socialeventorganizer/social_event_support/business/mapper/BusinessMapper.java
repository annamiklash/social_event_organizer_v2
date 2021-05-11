package pjatk.socialeventorganizer.social_event_support.business.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.Business;
import pjatk.socialeventorganizer.social_event_support.business.model.request.CreateBusinessAccountRequest;
import pjatk.socialeventorganizer.social_event_support.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.user.model.User;


@Component
public class BusinessMapper {

    public Business mapToDTO(CreateBusinessAccountRequest businessRequest, User user) {

        return Business.builder()
                .id(user.getId())
                .firstName(businessRequest.getFirstName())
                .lastName(businessRequest.getLastName())
                .businessName(businessRequest.getBusinessName())
                .phoneNumber(Converter.convertPhoneNumberString(businessRequest.getPhoneNumber()))
                .verificationStatus(String.valueOf(BusinessVerificationStatusEnum.NOT_VERIFIED))
                .user(user)
                .build();
    }
}
