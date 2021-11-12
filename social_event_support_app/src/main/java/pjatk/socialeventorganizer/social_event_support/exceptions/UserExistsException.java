package pjatk.socialeventorganizer.social_event_support.exceptions;

public class UserExistsException extends RuntimeException {

    public enum ENUM {
        USER_EXISTS
    }

    public UserExistsException(Enum message) {
        super(String.valueOf(message));
    }

}
