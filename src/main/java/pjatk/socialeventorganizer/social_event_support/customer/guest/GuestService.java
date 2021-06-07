package pjatk.socialeventorganizer.social_event_support.customer.guest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.invite.mapper.GuestInfoMapper;
import pjatk.socialeventorganizer.social_event_support.invite.response.GuestInfoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GuestService {

    GuestRepository guestRepository;

    GuestInfoMapper guestInfoMapper;

    public List<GuestInfoResponse> getGuestsByOrganizedEventId(long organizedEventId) {
        final Optional<List<Guest>> optionalGuestList = guestRepository.findGuestsByOrganizedEventId(organizedEventId);
        if (!optionalGuestList.isPresent()) {
            return new ArrayList<>();
        }
        final List<Guest> guestList = optionalGuestList.get();
        return guestList.stream()
                .map(guest -> guestInfoMapper.mapToResponse(guest))
                .collect(Collectors.toList());
    }

}
