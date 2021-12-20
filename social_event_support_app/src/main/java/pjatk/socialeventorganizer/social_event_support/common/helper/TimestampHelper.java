package pjatk.socialeventorganizer.social_event_support.common.helper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimestampHelper {

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

}
