package pjatk.socialeventorganizer.social_event_support.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@UtilityClass
public class DateTimeUtil {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String TIME_FORMAT_WITH_SECONDS = "HH:mm:ss";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER_WITH_SECONDS = DateTimeFormatter.ofPattern(TIME_FORMAT_WITH_SECONDS);

    public LocalDateTime fromStringToFormattedDateTime(String date) {
        if (date == null || date.equals("null")) {
            return null;
        }

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
        if (dateTime == null) {
            return null;
        }

        log.info(String.valueOf(dateTime));

        return dateTime.format(DATE_TIME_FORMATTER);
    }

    public LocalDate parseFromString(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    public String joinDateAndTime(String date, String time) {
        if (time.length() != TIME_FORMAT_WITH_SECONDS.length()) {
            time = time + ":00";
        }

        return date + " " + time;
    }

    public String fromLocalTimeToString(LocalTime time) {
        return time.format(TIME_FORMATTER_WITH_SECONDS);
    }


    public String toDateOnlyStringFromLocalDateTime(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public String toTimeOnlyStringFromLocalDateTime(LocalDateTime dateTime) {
        final LocalTime localTime = dateTime.toLocalTime();

        return localTime.format(TIME_FORMATTER);
    }

    public String toTimeOnlyFromLocalTime(LocalTime localTime) {

        return localTime.format(TIME_FORMATTER);
    }

    public LocalTime toLocalTimeFromTimeString(String time) {
        if (time == null) {
            return null;
        }
        return LocalTime.parse(time,
                DateTimeFormatter.ofPattern(TIME_FORMAT_WITH_SECONDS));
    }

    public Time toTimeFromString(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Time formattedTime = null;
        try {
            if (time != null) {
                long ms = sdf.parse(time).getTime();
                formattedTime = new Time(ms);
                return formattedTime;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String fromTimeToString(Time time) {
        String pattern = "HH:mm:ss";

        DateFormat df = new SimpleDateFormat(pattern);

// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        return df.format(time);
    }
}
