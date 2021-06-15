package pjatk.socialeventorganizer.social_event_support.invite.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.invite.response.ServicesInfoResponse;
import pjatk.socialeventorganizer.social_event_support.serviceforevent.ServiceForEvent;

@Component
public class OptionalServiceInfoMapper {

    public ServicesInfoResponse mapToResponse(ServiceForEvent optionalServiceForEvent) {


        return ServicesInfoResponse.builder()
                .serviceAlias(optionalServiceForEvent.getOptionalService().getAlias())
                .comment(optionalServiceForEvent.getComment())
                .dateTimeFrom(optionalServiceForEvent.getDateTimeFrom())
                .dateTimeTo(optionalServiceForEvent.getDateTimeTo())
                .optionalServiceId(optionalServiceForEvent.getOptionalService().getId())
                .build();
    }
}
