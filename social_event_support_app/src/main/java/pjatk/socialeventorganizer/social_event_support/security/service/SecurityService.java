package pjatk.socialeventorganizer.social_event_support.security.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository;
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.enums.UserTypeEnum;
import pjatk.socialeventorganizer.social_event_support.security.model.AuthenticationToken;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.password.PasswordEncoderSecurity;
import pjatk.socialeventorganizer.social_event_support.user.login.model.request.LoginDto;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SecurityService {

    PasswordEncoderSecurity passwordEncoderUtility;

    UserService userService;

    BusinessRepository businessRepository;

    CustomerRepository customerRepository;

    public boolean isPasswordMatch(LoginDto loginDto) {
        final String passwordFromRequest = loginDto.getPassword();

        final User userFromDb = userService.getUserByEmail(loginDto.getEmail());
        final String hashedPasswordFromDB = userFromDb.getPassword();

        return passwordEncoderUtility.doPasswordsMatch(passwordFromRequest, hashedPasswordFromDB);
    }

    public void buildSecurityContext(LoginDto loginDto, HttpServletRequest request) {
        final User user = userService.getUserByEmail(loginDto.getEmail());
        final UserCredentials userCredentials = UserCredentials.builder()
                .login(loginDto.getEmail())
                .userId(user.getId())
                .userType(user.getType())
                .build();

        final List<String> authorities = getAuthorities(userCredentials);
        final List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        final AuthenticationToken authenticationToken = new AuthenticationToken(grantedAuthorities, userCredentials);
        authenticationToken.setAuthenticated(true);
        final SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);

        final HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);


    }

    private boolean isNewAccount(Long id, char type) {
        return userService.isNewAccount(id, type);
    }

    public UserCredentials getUserCredentials() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final AuthenticationToken authentication = (AuthenticationToken) context.getAuthentication();
        return authentication.getCredentials();
    }

    private List<String> getAuthorities(UserCredentials userCredentials) {
        final List<String> authorities = new ArrayList<>();
        if (userCredentials.getUserType().equals(UserTypeEnum.BUSINESS.getValue())) {
            authorities.add("BUSINESS");
        } else if (userCredentials.getUserType().equals(UserTypeEnum.CUSTOMER.getValue())) {
            authorities.add("CUSTOMER");
        } else if (userCredentials.getUserType().equals(UserTypeEnum.ADMIN.getValue())) {
            authorities.add("ADMIN");
        }

        return authorities;
    }

}
