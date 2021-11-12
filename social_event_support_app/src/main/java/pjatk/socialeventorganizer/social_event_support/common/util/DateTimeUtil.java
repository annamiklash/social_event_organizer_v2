package pjatk.socialeventorganizer.social_event_support.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@UtilityClass
public class DateTimeUtil {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public LocalDateTime fromStringToFormattedDateTime(String date) {
        log.info(date);

        if (date == null || date.equals("null")) {
            return null;
        }
        log.info(date);

        if (date.contains("T")) {
            date = date.replace("T", " ");
        }

        if (date.length() > DATE_TIME_FORMAT.length()) {
            date = date.substring(0, DATE_TIME_FORMAT.length());
        }
        return LocalDateTime.parse(date, DATE_TIME_FORMATTER);

    }

    public LocalDate fromStringToFormattedDate(String date) {
        if (date == null || date.equals("null")) {
            return null;
        }

        return LocalDate.parse(date, DATE_FORMATTER);
    }

    public String toStringFromLocalDateTime(LocalDateTime dateTime) {
        log.info(String.valueOf(dateTime));

        return dateTime.format(DATE_TIME_FORMATTER);
    }

    public LocalDate parseFromString(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }


    public String toDateOnlyStringFromLocalDateTime(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public String toTimeOnlyStringFromLocalDateTime(LocalDateTime dateTime) {
        final LocalTime localTime = dateTime.toLocalTime();

        return localTime.format(TIME_FORMATTER);
    }
}
