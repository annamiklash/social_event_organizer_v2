package pjatk.socialeventorganizer.social_event_support.security.enums;

import java.util.HashMap;
import java.util.Map;

public enum AllowedUrlsEnum {

    REGISTER("/api/register/**"),
    LOGIN("/api/login*"),
    LOGOUT("/api/logout*"),
    LOCATIONS("/api/locations/allowed/**"),
    LOCATION_AVAILABILITY("api/availability/location/allowed**"),
    CATERINGS("/api/caterings/allowed/**"),
    CATERING_AVAILABILITY("api/availability/catering/allowed**"),
    SERVICES("/api/services/allowed/**"),
    SERVICES_AVAILABILITY("api/availability/services/allowed**"),
    CUISINES("/api/cuisines/allowed/**"),
    RESET_PASSWORD("/api/reset_password"),
    RESET( "/api/reset");

    public final String value;

    AllowedUrlsEnum(String value) {
        this.value = value;
    }

    private static final Map<String, AllowedUrlsEnum> VALUES = new HashMap<>();

    static {
        for (AllowedUrlsEnum e : values()) {
            VALUES.put(e.value, e);
        }
    }

    public static AllowedUrlsEnum valueOfLabel(String label) {
        return VALUES.get(label);
    }
    }