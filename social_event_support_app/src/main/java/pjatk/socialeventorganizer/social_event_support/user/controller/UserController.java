package pjatk.socialeventorganizer.social_event_support.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.user.model.request.NewPasswordRequest;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Validated
@RestController
@RequestMapping("api")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/reset_password")
    public ResponseEntity<Void> sendResetPasswordEmail(HttpServletRequest request, @RequestParam String email) {
        final String appUrl = request.getScheme() + "://" + request.getServerName();
        userService.sendResetEmailLink(email, appUrl);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> setNewPassword(@RequestParam("token") String token, @RequestBody NewPasswordRequest newPasswordRequest) {

        userService.setNewPassword(token, newPasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/block")
    public ResponseEntity<Void> block(@PathVariable long id) {
        userService.block(id);


        return ResponseEntity.ok().build();
    }



}
