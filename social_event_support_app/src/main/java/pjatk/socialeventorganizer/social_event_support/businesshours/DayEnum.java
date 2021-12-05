package pjatk.socialeventorganizer.social_event_support.businesshours;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum DayEnum implements Serializable {

    MONDAY("Monday"), TUESDAY("Tuesday"), WEDNESDAY("Wednesday"), THURSDAY("Thursday"), FRIDAY("Friday"), SATURDAY("Saturday"), SUNDAY("Sunday");

    private final String value;

    DayEnum(String value) {
        this.value = value;
    }

    private static final Map<String, DayEnum> VALUES = new HashMap<>();

    static {
        for (DayEnum e : values()) {
            VALUES.put(e.value, e);
        }
    }

    public static DayEnum valueOfLabel(String label) {
        return VALUES.get(label);
    }

}
