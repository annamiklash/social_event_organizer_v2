package pjatk.socialeventorganizer.social_event_support.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.appproblem.AppProblem;
import pjatk.socialeventorganizer.social_event_support.appproblem.mapper.AppProblemMapper;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblemDto;
import pjatk.socialeventorganizer.social_event_support.user.model.request.NewPasswordRequest;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/report",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppProblemDto> reportProblem(@Valid @RequestBody AppProblemDto dto) {

        final AppProblem appProblem = userService.reportProblem(dto);

        return ResponseEntity.ok(AppProblemMapper.toDtoWithUser(appProblem));
    }


}
