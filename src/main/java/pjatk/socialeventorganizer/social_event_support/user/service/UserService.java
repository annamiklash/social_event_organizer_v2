package pjatk.socialeventorganizer.social_event_support.user.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.exceptions.InvalidCredentialsException;
import pjatk.socialeventorganizer.social_event_support.exceptions.PasswordsDontMatchException;
import pjatk.socialeventorganizer.social_event_support.exceptions.UserExistsException;
import pjatk.socialeventorganizer.social_event_support.security.password.PasswordEncoderSecurity;
import pjatk.socialeventorganizer.social_event_support.user.mapper.UserMapper;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.model.request.NewPasswordRequest;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.RegisterRequest;
import pjatk.socialeventorganizer.social_event_support.user.repository.UserRepository;
import pjatk.socialeventorganizer.social_event_support.util.EmailUtil;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoderSecurity passwordEncoderSecurity;

    private final UserMapper userMapper;

    private final EmailService emailService;

    public ImmutableList<User> findALl() {
        return ImmutableList.copyOf(userRepository.findAll());
    }

    public User getUserByEmail(String email) {
        final Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new InvalidCredentialsException("Please check credentials");
    }

    public void save(User user) {
        userRepository.save(user);
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

    public User getByResetPasswordToken(String token) {
        return userRepository.findUserByResetPasswordToken(token);
    }

    @Transactional
    public void sendResetEmailLink(String email, String appUrl) {
        final User user = getUserByEmail(email);
        user.setResetPasswordToken(UUID.randomUUID().toString());
        save(user);

        final String emailSubject = "Password Reset Request";
        final String content = "To reset your password, click the link below:\n" + appUrl
                + "/reset?token=" + user.getResetPasswordToken() + "\n\nSent via SocialEventOrganizer app";
        final SimpleMailMessage passwordResetEmail = EmailUtil.emailBuilder(content, email, emailSubject);

        log.info("EMAIL: " + passwordResetEmail.toString());

        emailService.sendEmail(passwordResetEmail);
    }

    @Transactional
    public void setNewPassword(String token, NewPasswordRequest newPasswordRequest) {
        final User user = getByResetPasswordToken(token);

        if (!passwordsMatch(newPasswordRequest.getPassword(), newPasswordRequest.getConfirmPassword())) {
            throw new PasswordsDontMatchException("Passwords provided dont match");
        }

        user.setPassword(passwordEncoderSecurity.bcryptEncryptor(newPasswordRequest.getPassword()));
        user.setResetPasswordToken(null);
        save(user);
    }

    private boolean passwordsMatch(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

    private boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
