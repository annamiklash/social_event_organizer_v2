package pjatk.socialeventorganizer.social_event_support.user.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.appproblem.AppProblem;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblemDto;
import pjatk.socialeventorganizer.social_event_support.appproblem.service.AppProblemService;
import pjatk.socialeventorganizer.social_event_support.common.util.EmailUtil;
import pjatk.socialeventorganizer.social_event_support.exceptions.InvalidCredentialsException;
import pjatk.socialeventorganizer.social_event_support.exceptions.UserExistsException;
import pjatk.socialeventorganizer.social_event_support.security.password.PasswordEncoderSecurity;
import pjatk.socialeventorganizer.social_event_support.user.mapper.UserMapper;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.model.request.NewPasswordRequest;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.UserDto;
import pjatk.socialeventorganizer.social_event_support.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static pjatk.socialeventorganizer.social_event_support.exceptions.InvalidCredentialsException.Enum.INCORRECT_CREDENTIALS;
import static pjatk.socialeventorganizer.social_event_support.exceptions.InvalidCredentialsException.Enum.USER_NOT_EXISTS;
import static pjatk.socialeventorganizer.social_event_support.exceptions.UserExistsException.ENUM.USER_EXISTS;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoderSecurity passwordEncoderSecurity;

    private final EmailService emailService;

    private final AppProblemService appProblemService;

    public ImmutableList<User> findALl() {
        return ImmutableList.copyOf(userRepository.findAll());
    }

    public User getUserByEmail(String email) {
        final Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new InvalidCredentialsException(INCORRECT_CREDENTIALS);
    }

    public void save(User user) {

        userRepository.save(user);
    }

    public User getById(Long id) {
        final Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new InvalidCredentialsException(USER_NOT_EXISTS);
    }

    public void registerUser(UserDto dto) {
        if (userExists(dto.getEmail())) {
            throw new UserExistsException(USER_EXISTS);
        }
        final String hashedPassword = passwordEncoderSecurity.bcryptEncryptor(dto.getPassword());
        dto.setPassword(hashedPassword);

        final User user = UserMapper.fromDto(dto);

        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());

        userRepository.save(user);
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

        user.setPassword(passwordEncoderSecurity.bcryptEncryptor(newPasswordRequest.getPassword()));
        user.setResetPasswordToken(null);
        save(user);
    }

    public boolean isNewAccount(long id, char type) {
        final Optional<User> optionalUser = userRepository.isNewAccount(id, type);
        if (optionalUser.isPresent()) {
            return false;
        }
        return true;
    }

    private boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }


    public void block(long id) {

        final User user = getById(id);
        user.setActive(false);
        user.setBlockedAt(LocalDateTime.now());

        //TODO: invalidate users session

        save(user);
    }

    public AppProblem reportProblem(AppProblemDto dto, long id) {
        final User user = getById(id);

        return appProblemService.create(dto, user);

    }
}
