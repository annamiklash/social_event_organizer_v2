package pjatk.socialeventorganizer.social_event_support.user.controller;

import com.google.common.collect.ImmutableList;
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
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.user.mapper.UserMapper;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.model.request.NewPasswordRequest;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.UserDto;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.stream.Collectors;

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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "{id}/report",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppProblemDto> reportProblem(@Valid @RequestBody AppProblemDto dto, @PathVariable long id) {

        final AppProblem appProblem = userService.reportProblem(dto, id);

        return ResponseEntity.ok(AppProblemMapper.toDtoWithUser(appProblem));
    }
    @RequestMapping(
            method = RequestMethod.GET,
            path = "users/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<UserDto>> list(@RequestParam(required = false) String keyword,
                                                       @RequestParam(defaultValue = "0") Integer firstResult,
                                                       @RequestParam(defaultValue = "6") Integer maxResult,
                                                       @RequestParam(defaultValue = "id") String sort,
                                                       @RequestParam(defaultValue = "desc") String order) {
        log.info("GET ALL USERS");

        final ImmutableList<User> list = userService.list(new CustomPage(maxResult, firstResult, sort, order), keyword);

        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(UserMapper::toDto)
                        .collect(Collectors.toList())));
    }


}
