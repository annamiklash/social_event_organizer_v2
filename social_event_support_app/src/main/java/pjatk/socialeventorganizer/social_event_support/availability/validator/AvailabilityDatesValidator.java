package pjatk.socialeventorganizer.social_event_support.availability.validator;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class AvailabilityDatesValidator {

    public void validate(String date, String timeFrom, String timeTo) {
        final LocalDate localDate = DateTimeUtil.fromStringToFormattedDate(date);

        final LocalDateTime from = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(date, timeFrom));
        final LocalDateTime to = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(date, timeTo));

        if (!localDate.equals(from.toLocalDate()) || !localDate.equals(to.toLocalDate())){
            throw new IllegalArgumentException("Date not same as date in timestamp");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Date from after till");
        }
    }
}
