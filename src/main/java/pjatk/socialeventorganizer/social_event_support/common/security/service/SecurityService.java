package pjatk.socialeventorganizer.social_event_support.common.security.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.common.security.model.AuthenticationToken;
import pjatk.socialeventorganizer.social_event_support.common.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.common.security.password.PasswordEncoderSecurity;
import pjatk.socialeventorganizer.social_event_support.model.dto.Business;
import pjatk.socialeventorganizer.social_event_support.model.dto.Customer;
import pjatk.socialeventorganizer.social_event_support.model.dto.User;
import pjatk.socialeventorganizer.social_event_support.model.enums.UserTypeEnum;
import pjatk.socialeventorganizer.social_event_support.model.request.LoginRequest;
import pjatk.socialeventorganizer.social_event_support.repository.BusinessRepository;
import pjatk.socialeventorganizer.social_event_support.repository.CustomerRepository;
import pjatk.socialeventorganizer.social_event_support.service.BusinessService;
import pjatk.socialeventorganizer.social_event_support.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SecurityService {

    PasswordEncoderSecurity passwordEncoderUtility;

    UserService userService;

    CustomerRepository customerRepository;

    BusinessRepository businessRepository;


    public boolean isPasswordMatch(LoginRequest loginRequest) {
        final String passwordFromRequest = loginRequest.getPassword();

        final User userFromDb = userService.getUserByEmail(loginRequest.getEmail());
        final String hashedPasswordFromDB = userFromDb.getPassword();

        return passwordEncoderUtility.doPasswordsMatch(passwordFromRequest, hashedPasswordFromDB);
    }

    public void buildSecurityContext(LoginRequest loginRequest, HttpServletRequest request) {
        final User user = userService.getUserByEmail(loginRequest.getEmail());
        log.info(user.toString());
        final UserCredentials userCredentials = UserCredentials.builder()
                .login(loginRequest.getEmail())
                .userType(user.getType())
                .build();
        assignUserType(userCredentials);

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

    private void assignUserType(UserCredentials userCredentials) {

        final Character userType = userCredentials.getUserType();
        switch (userType) {
            case 'C':
                final long customerUserId = customerRepository.getCustomerUserId(userCredentials.getLogin());
                userCredentials.setUserId(customerUserId);

                final long customerId = customerRepository.getCustomerCustomerIdId(userCredentials.getLogin());
                userCredentials.setCustomerId(customerId);
                break;
            case 'B':
                final long businessUserId = businessRepository.getBusinessUserId(userCredentials.getLogin());
                userCredentials.setUserId(businessUserId);

                final long businessId = businessRepository.getBusinessBusinessID(userCredentials.getLogin());
                userCredentials.setCustomerId(businessId);
                break;
            case 'A':
                break;

        }
    }

    public UserCredentials getUserCredentials() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final AuthenticationToken authentication = (AuthenticationToken) context.getAuthentication();
        return authentication.getCredentials();
    }

    private List<String> getAuthorities(UserCredentials userCredentials) {
        if (userCredentials.getUserType().equals(UserTypeEnum.BUSINESS.getValue())) {
            return Arrays.asList("BUSINESS");
        } else if (userCredentials.getUserType().equals(UserTypeEnum.CUSTOMER.getValue())) {
            return Arrays.asList("CUSTOMER");
        }else if (userCredentials.getUserType().equals(UserTypeEnum.ADMIN.getValue())) {
            return Arrays.asList("ADMIN");
        }

        throw new IllegalStateException();
    }

}
