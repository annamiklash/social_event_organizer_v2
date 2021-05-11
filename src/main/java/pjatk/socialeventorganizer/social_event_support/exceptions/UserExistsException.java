package pjatk.socialeventorganizer.social_event_support.exceptions;

public class UserExistsException extends RuntimeException{

    public UserExistsException(String message) {
        super(message);
    }
}
