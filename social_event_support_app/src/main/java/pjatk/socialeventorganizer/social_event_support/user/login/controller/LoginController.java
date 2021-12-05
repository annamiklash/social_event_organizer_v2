package pjatk.socialeventorganizer.social_event_support.user.login.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.user.login.model.request.LoginDto;
import pjatk.socialeventorganizer.social_event_support.user.mapper.UserMapper;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.UserDto;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class LoginController {

    private final SecurityService securityService;

    private final UserService userService;

    @RequestMapping(
            method = RequestMethod.POST,
            path = "login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> login(@RequestBody @Valid LoginDto loginDto, HttpServletRequest request) {
        request.getSession().invalidate();

        if (!userService.isActive(loginDto)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (securityService.isPasswordMatch(loginDto)) {
            securityService.buildSecurityContext(loginDto, request);
            return ResponseEntity.ok(
                    UserMapper.toDto(userService.getUserByEmail(loginDto.getEmail())));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "logout")
    ResponseEntity<Void> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok().build();
    }

}