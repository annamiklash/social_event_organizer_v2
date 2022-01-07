package pjatk.socialeventorganizer.social_event_support.user.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository;
import pjatk.socialeventorganizer.social_event_support.common.mapper.PageableMapper;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.common.util.EmailUtil;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.InvalidCredentialsException;
import pjatk.socialeventorganizer.social_event_support.security.password.PasswordEncoderSecurity;
import pjatk.socialeventorganizer.social_event_support.user.mapper.UserMapper;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.ChangePasswordDto;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.LoginDto;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.NewPasswordDto;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.UserDto;
import pjatk.socialeventorganizer.social_event_support.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.exceptions.InvalidCredentialsException.Enum.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoderSecurity passwordEncoderSecurity;

    private final EmailService emailService;

    private final CustomerRepository customerRepository;

    private final BusinessRepository businessRepository;


    public ImmutableList<User> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<User> page = userRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(INCORRECT_CREDENTIALS));
    }

    public void save(User user) {

        userRepository.save(user);
    }

    public User get(long id) {
        final Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new InvalidCredentialsException(USER_NOT_EXISTS);
    }

    public UserDto getWithDetail(long id) {
        final User user = get(id);
        if (user.getType() == 'C') {
            final Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new InvalidCredentialsException(USER_NOT_EXISTS));
            return UserMapper.toDtoWithCustomer(user, customer);

        } else if (user.getType() == 'B') {
            final Business business = businessRepository.findById(id)
                    .orElseThrow(() -> new InvalidCredentialsException(USER_NOT_EXISTS));
            return UserMapper.toDtoWithBusiness(user, business);

        } else if (user.getType() == 'A') {
            return UserMapper.toDto(user);
        }
        throw new InvalidCredentialsException(USER_NOT_EXISTS);
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findUserByResetPasswordToken(token);
    }

    @Transactional(rollbackOn = Exception.class)
    public void sendResetEmailLink(String email, String appUrl) {
        final User user = getUserByEmail(email);
        user.setResetPasswordToken(UUID.randomUUID().toString());
        save(user);

        final String emailSubject = "Password Reset Request";
        final String content = "To reset your password, click the link below:\n" + appUrl
                + "/reset?token=" + user.getResetPasswordToken() + "\n\nSent via SocialEventOrganizer app";
        final SimpleMailMessage passwordResetEmail = EmailUtil.buildEmail(content, email, emailSubject);

        log.info("EMAIL: " + passwordResetEmail.toString());

        emailService.sendEmail(passwordResetEmail);
    }

    public void changePassword(long id, ChangePasswordDto dto) {
        final User user = get(id);
        final Boolean passwordsMatch = passwordEncoderSecurity.doPasswordsMatch(dto.getOldPassword(), user.getPassword());
        if (!passwordsMatch) {
            throw new InvalidCredentialsException(PASSWORDS_NOT_MATCH);
        }
        user.setPassword(passwordEncoderSecurity.bcryptEncryptor(dto.getNewPassword()));
        save(user);
    }

    public void setNewPassword(String token, NewPasswordDto newPasswordDto) {
        final User user = getByResetPasswordToken(token);

        user.setPassword(passwordEncoderSecurity.bcryptEncryptor(newPasswordDto.getPassword()));
        user.setResetPasswordToken(null);
        save(user);
    }

    public boolean isNewAccount(long id, char type) {
        final Optional<User> optionalUser = userRepository.isNewAccount(id, type);
        return optionalUser.isEmpty();
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public void block(long id) {
        final User user = get(id);
        user.setActive(false);
        user.setBlockedAt(LocalDateTime.now());

        save(user);
    }

    public boolean isActive(LoginDto loginDto) {
        return userRepository.active(loginDto.getEmail()).isPresent();
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public Long count(String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();
        return userRepository.countAll(keyword);
    }
}
