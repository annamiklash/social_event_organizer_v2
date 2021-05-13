package pjatk.socialeventorganizer.social_event_support.user.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.exceptions.InvalidCredentialsException;
import pjatk.socialeventorganizer.social_event_support.exceptions.PasswordsDontMatchException;
import pjatk.socialeventorganizer.social_event_support.exceptions.UserExistsException;
import pjatk.socialeventorganizer.social_event_support.security.password.PasswordEncoderSecurity;
import pjatk.socialeventorganizer.social_event_support.user.mapper.UserMapper;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.RegisterRequest;
import pjatk.socialeventorganizer.social_event_support.user.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    UserRepository userRepository;

    PasswordEncoderSecurity passwordEncoderSecurity;

    UserMapper userMapper;

    public ImmutableList<User> findALl(){
        return ImmutableList.copyOf(userRepository.findAll());
    }

    public User getUserByEmail(String email) {
        final Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new InvalidCredentialsException("Please check log in credentials");
    }

    public User getUserById(Long id) {
        final Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        //TODO: add custom exception
        throw new InvalidCredentialsException("User with id " + id + " does not exist");
    }

    public void registerUser(RegisterRequest request) {
        if (!passwordsMatch(request.getPassword(), request.getPasswordConfirmation())) {
            throw new PasswordsDontMatchException("Passwords provided dont match");
        }
        if (userExists(request.getEmail())) {
            throw new UserExistsException("User with the same login/email already exists. Choose another one");
        }
        final String hashedPassword = passwordEncoderSecurity.bcryptEncryptor(request.getPassword());
        final User user = userMapper.mapToDTO(request, hashedPassword);
        userRepository.save(user);

        //TODO: SEND EMAIL WITH CONF LINK
    }

    private boolean passwordsMatch(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

    private boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

}
