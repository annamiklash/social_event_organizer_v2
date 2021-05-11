package pjatk.socialeventorganizer.social_event_support.user.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.RegisterRequest;

@Component
public class UserMapper {

    public User mapToDTO(RegisterRequest request, String hashedPassword) {

        return User.builder()
                .email(request.getEmail())
                .password(hashedPassword)
                .type(request.getUserType())
                .build();

    }
}
