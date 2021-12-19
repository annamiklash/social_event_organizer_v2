package pjatk.socialeventorganizer.social_event_support.common.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimestampUtil {

    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
