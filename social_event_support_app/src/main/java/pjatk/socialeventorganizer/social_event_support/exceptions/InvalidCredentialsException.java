package pjatk.socialeventorganizer.social_event_support.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    public enum Enum {
        INCORRECT_CREDENTIALS, USER_NOT_EXISTS, PASSWORDS_NOT_MATCH
    }
    public InvalidCredentialsException(Enum message) {
        super(String.valueOf(message));
    }
}
