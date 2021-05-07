package pjatk.socialeventorganizer.social_event_support.mapper;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.model.dto.Catering;
import pjatk.socialeventorganizer.social_event_support.model.request.CateringRequest;
import pjatk.socialeventorganizer.social_event_support.model.response.CateringResponse;

import java.math.BigDecimal;
import java.math.BigInteger;

@Component
@Slf4j
public class CateringMapper {

    public Catering mapToDTO(CateringRequest request, Address address) {

        final String requestServiceCost = request.getServiceCost();
        log.info("COST STRING " + requestServiceCost);

        final BigDecimal serviceCost = Converter.convertPriceString(requestServiceCost);
        log.info("COST CONVERTED " + serviceCost);

        final String requestPhoneNumber = request.getPhoneNumber();
        final BigInteger phoneNumber = Converter.convertPhoneNumberString(requestPhoneNumber);

        final String requestDescription = request.getDescription();
        final String description = Converter.convertDescriptionsString(requestDescription);

        return Catering.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(phoneNumber)
                .serviceCost(serviceCost)
                .description(description)
                .businessId(request.getBusinessId())
                .cateringAddress(address)
                .build();
    }

    public void updateDTO(CateringRequest request, Catering fetchedCatering) {
        final String requestServiceCost = request.getServiceCost();
        final BigDecimal serviceCost = Converter.convertPriceString(requestServiceCost);

        final String requestPhoneNumber = request.getPhoneNumber();
        final BigInteger phoneNumber = Converter.convertPhoneNumberString(requestPhoneNumber);

        final String requestDescription = request.getDescription();
        final String description = Converter.convertDescriptionsString(requestDescription);

        fetchedCatering.setName(request.getName());
        fetchedCatering.setEmail(request.getEmail());
        fetchedCatering.setPhoneNumber(phoneNumber);
        fetchedCatering.setServiceCost(serviceCost);
        fetchedCatering.setDescription(description);

    }

    public CateringResponse mapToResponse(Catering catering) {
        return CateringResponse.builder()
                .id(catering.getId())
                .name(catering.getName())
                .email(catering.getEmail())
                .build();
    }
}
