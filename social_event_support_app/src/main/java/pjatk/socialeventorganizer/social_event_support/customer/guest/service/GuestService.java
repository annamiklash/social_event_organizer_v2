package pjatk.socialeventorganizer.social_event_support.customer.guest.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.customer.guest.mapper.GuestMapper;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto;
import pjatk.socialeventorganizer.social_event_support.customer.guest.repository.GuestRepository;
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
        final Optional<List<Guest>> optionalGuestList = guestRepository.findInvitedGuestsByOrganizedEventId(organizedEventId);
        if (!optionalGuestList.isPresent()) {
            return new ArrayList<>();
        }
        final List<Guest> guestList = optionalGuestList.get();
        return guestList.stream()
                .map(guest -> guestInfoMapper.mapToResponse(guest))
                .collect(Collectors.toList());
    }

    public ImmutableList<GuestDto> list(CustomPage customPagination, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageRequest.of(customPagination.getFirstResult(), customPagination.getMaxResult(), Sort.by(customPagination.getSort()).descending());
        final Page<Guest> page = guestRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().map(GuestMapper::toDto).collect(Collectors.toList()));
    }

}
