package pjatk.socialeventorganizer.social_event_support.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.model.dto.Guest;
import pjatk.socialeventorganizer.social_event_support.repository.GuestRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class GuestService {

    GuestRepository guestRepository;

}
