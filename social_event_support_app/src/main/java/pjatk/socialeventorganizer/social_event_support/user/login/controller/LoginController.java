package pjatk.socialeventorganizer.social_event_support.user.login.controller;

import com.google.common.collect.ImmutableList;
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
import pjatk.socialeventorganizer.social_event_support.user.model.User;
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
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> login(@RequestBody @Valid LoginDto loginDto, HttpServletRequest request) {
        request.getSession().invalidate();

        if (securityService.isPasswordMatch(loginDto)) {
            securityService.buildSecurityContext(loginDto, request);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/logout")
    ResponseEntity<Void> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok().build();
    }


    @RequestMapping(
            method = RequestMethod.GET,
            value = "/users")
    ResponseEntity<ImmutableList<User>> getALlUsers() {
        return ResponseEntity.ok(userService.findALl());
    }

}