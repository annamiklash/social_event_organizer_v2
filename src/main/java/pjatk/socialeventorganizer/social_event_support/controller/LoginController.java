package pjatk.socialeventorganizer.social_event_support.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.common.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.model.request.LoginRequest;

import javax.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("api")
@AllArgsConstructor
public class LoginController {

    private final SecurityService securityService;

    @PostMapping("login")
    ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        request.getSession().invalidate();

        if (securityService.isPasswordMatch(loginRequest)) {
            securityService.buildSecurityContext(loginRequest, request);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @GetMapping("logout")
    ResponseEntity<Void> logout( HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok().build();
    }

}