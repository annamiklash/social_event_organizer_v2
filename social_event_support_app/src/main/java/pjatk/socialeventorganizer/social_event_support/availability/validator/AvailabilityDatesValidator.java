package pjatk.socialeventorganizer.social_event_support.availability.validator;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;

import java.time.LocalDateTime;

@UtilityClass
public class AvailabilityDatesValidator {

    public void validate(String dateFrom, String dateTo) {
        final LocalDateTime from = DateTimeUtil.fromStringToFormattedDateTime(dateFrom);
        final LocalDateTime to = DateTimeUtil.fromStringToFormattedDateTime(dateTo);

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Incorrect time provided");
        }
    }
}
