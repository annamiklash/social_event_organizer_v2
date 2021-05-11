package pjatk.socialeventorganizer.social_event_support.user.registration.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.RegisterRequest;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api")
public class RegisterController {

    private final UserService userService;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        userService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
