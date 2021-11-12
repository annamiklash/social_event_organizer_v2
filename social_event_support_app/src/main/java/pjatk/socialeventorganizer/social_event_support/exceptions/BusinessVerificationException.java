package pjatk.socialeventorganizer.social_event_support.exceptions;

public class BusinessVerificationException extends RuntimeException{

    public enum Enum {
        BUSINESS_NOT_VERIFIED, ALREADY_VERIFIED
    }

    public BusinessVerificationException(Enum message) {
        super(String.valueOf(message));
    }
}
