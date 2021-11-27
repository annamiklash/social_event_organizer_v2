package pjatk.socialeventorganizer.social_event_support.optional_service.model.music.musicstyle.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.music.musicstyle.repository.MusicStyleRepository;

@Service
@AllArgsConstructor
@Slf4j
public class MusicStyleService {

    private final MusicStyleRepository musicStyleRepository;

}
