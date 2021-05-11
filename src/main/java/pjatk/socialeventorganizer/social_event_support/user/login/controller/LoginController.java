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
import pjatk.socialeventorganizer.social_event_support.user.login.model.request.LoginRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class LoginController {

    private final SecurityService securityService;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        request.getSession().invalidate();

        if (securityService.isPasswordMatch(loginRequest)) {
            securityService.buildSecurityContext(loginRequest, request);
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

}