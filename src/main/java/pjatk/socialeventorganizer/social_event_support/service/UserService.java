package pjatk.socialeventorganizer.social_event_support.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.model.dto.User;
import pjatk.socialeventorganizer.social_event_support.model.exception.InvalidCredentialsException;
import pjatk.socialeventorganizer.social_event_support.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    UserRepository userRepository;


    public User getUserByEmail(String email) {
        final Optional<User> optionalUser = userRepository.findDistinctByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new InvalidCredentialsException("Please check log in credentials");
    }
}
