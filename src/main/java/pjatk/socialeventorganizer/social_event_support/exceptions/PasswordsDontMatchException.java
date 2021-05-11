package pjatk.socialeventorganizer.social_event_support.exceptions;

public class PasswordsDontMatchException extends RuntimeException {

    public PasswordsDontMatchException(String message) {
        super(message);
    }
}