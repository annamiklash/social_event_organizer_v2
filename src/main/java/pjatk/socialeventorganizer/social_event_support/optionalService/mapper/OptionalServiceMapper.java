package pjatk.socialeventorganizer.social_event_support.optionalService.mapper;


import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.dto.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.request.OptionalServiceRequest;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.response.OptionalServiceInformationResponse;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.response.OptionalServiceResponse;

import java.util.HashSet;

@Component
public class OptionalServiceMapper {

    public OptionalService mapToDTO(OptionalServiceRequest request) {
        return OptionalService.builder()
                .alias(request.getAlias())
                .type(request.getType())
                .email(request.getEmail())
                .description(request.getDescription())
                .serviceCost(Converter.convertPriceString(request.getServiceCost()))
                .images(new HashSet<>())
                .build();
    }

    public OptionalServiceResponse mapToResponse(OptionalService optionalService) {
        return OptionalServiceResponse.builder()
                .id(optionalService.getId())
                .build();
    }

    public OptionalServiceInformationResponse mapDTOtoInformationResponse(OptionalService optionalService) {
        return OptionalServiceInformationResponse.builder()
                .id(Math.toIntExact(optionalService.getId()))
                .alias(optionalService.getAlias())
                .type(optionalService.getType())
                .email(optionalService.getEmail())
                .description(optionalService.getDescription())//probably just string
                .serviceCost(optionalService.getServiceCost())
                .build();
    }


}
