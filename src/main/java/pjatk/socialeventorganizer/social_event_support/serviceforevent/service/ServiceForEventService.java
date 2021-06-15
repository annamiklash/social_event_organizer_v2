package pjatk.socialeventorganizer.social_event_support.serviceforevent.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.invite.mapper.OptionalServiceInfoMapper;
import pjatk.socialeventorganizer.social_event_support.invite.response.ServicesInfoResponse;
import pjatk.socialeventorganizer.social_event_support.serviceforevent.ServiceForEvent;
import pjatk.socialeventorganizer.social_event_support.serviceforevent.repository.ServiceForEventRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceForEventService {

    private final ServiceForEventRepository serviceForEventRepository;

    private final OptionalServiceInfoMapper serviceInfoMapper;

    public List<ServicesInfoResponse> getServiceInfoLocationForEventId(long eventId) {
        final List<ServiceForEvent> servicesForEvent = serviceForEventRepository.findServiceForEventByLocationForEventId(eventId);
        return servicesForEvent.stream()
                .map(serviceInfoMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
