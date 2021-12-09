package pjatk.socialeventorganizer.social_event_support.availability.validator;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.availability.location.model.LocationAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class AvailabilityDatesValidator {


    public void validateLocationAvailability(String date, String timeFrom, String timeTo, List<LocationAvailability> availabilities) {
        final List<LocationAvailability> notAvailableList = availabilities.stream()
                .filter(locationAvailability -> locationAvailability.getStatus().equals("NOT_AVAILABLE"))
                .collect(Collectors.toList());

        final LocalDate localDate = DateTimeUtil.fromStringToFormattedDate(date);

        final LocalDateTime from = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(date, timeFrom));
        final LocalDateTime to = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(date, timeTo));

        if (!localDate.equals(from.toLocalDate()) || !localDate.equals(to.toLocalDate())) {
            throw new IllegalArgumentException("Date not same as date in timestamp");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Date from after till");
        }

        for (LocationAvailability availability : notAvailableList) {
            if (from.isBefore(availability.getTimeTo()) && from.isAfter(availability.getTimeFrom())) {
                throw new IllegalArgumentException("New timeFrom specified within not available time frame");
            }
            if (to.isBefore(availability.getTimeTo()) && to.isAfter(availability.getTimeFrom())) {
                throw new IllegalArgumentException("New timeTo specified within not available time frame");
            }
        }



    }

    public void validateServiiceAvailability(String date, String timeFrom, String timeTo, List<OptionalServiceAvailability> availabilities) {
        final List<OptionalServiceAvailability> list = availabilities.stream()
                .filter(locationAvailability -> locationAvailability.getStatus().equals("NOT_AVAILABLE")).collect(Collectors.toList());

        final LocalDate localDate = DateTimeUtil.fromStringToFormattedDate(date);
        final LocalDateTime from = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(date, timeFrom));
        final LocalDateTime to = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(date, timeTo));

        if (!localDate.equals(from.toLocalDate()) || !localDate.equals(to.toLocalDate())) {
            throw new IllegalArgumentException("Date not same as date in timestamp");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Date from after till");
        }

        for (OptionalServiceAvailability availability : list) {
            if (from.isBefore(availability.getTimeTo()) && from.isAfter(availability.getTimeFrom())) {
                throw new IllegalArgumentException("New timeFrom specified within not available time frame");
            }
            if (to.isBefore(availability.getTimeTo()) && to.isAfter(availability.getTimeFrom())) {
                throw new IllegalArgumentException("New timeTo specified within not available time frame");
            }
        }
    }
}
