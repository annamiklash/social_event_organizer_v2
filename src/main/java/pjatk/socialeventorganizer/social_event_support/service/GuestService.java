package pjatk.socialeventorganizer.social_event_support.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.repository.GuestRepository;

@Service
@AllArgsConstructor
@Slf4j
public class GuestService {

    GuestRepository guestRepository;
}
