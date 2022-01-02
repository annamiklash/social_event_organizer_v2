package pjatk.socialeventorganizer.social_event_support.user.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.table.TableDto;
import pjatk.socialeventorganizer.social_event_support.user.mapper.UserMapper;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.ChangePasswordDto;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.NewPasswordDto;
import pjatk.socialeventorganizer.social_event_support.user.registration.model.request.UserDto;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
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
        final String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api";
        //TODO: validate if user with email exists and not blocked
        log.warn(appUrl);
        userService.sendResetEmailLink(email, appUrl);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @PostMapping("password/change")
    public ResponseEntity<Void> changePassword(@RequestParam long id,
                                               @RequestBody ChangePasswordDto dto) {
        userService.changePassword(id, dto);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/reset")
    public ResponseEntity<Void> setNewPassword(@RequestParam("token") String token,
                                               @RequestBody NewPasswordDto newPasswordDto) {

        userService.setNewPassword(token, newPasswordDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{id}/block")
    public ResponseEntity<Void> block(@PathVariable long id) {
        userService.block(id);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "users/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<UserDto>> list(@RequestParam(required = false) String keyword,
                                                  @RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "50") Integer pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String order) {

        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build();

        final ImmutableList<User> users = userService.list(customPage, keyword);
        final Long count = userService.count(keyword);

        final ImmutableList<UserDto> result = ImmutableList.copyOf(
                users.stream()
                        .map(UserMapper::toDto)
                        .collect(Collectors.toList()));

        return ResponseEntity.ok(new TableDto<>(
                TableDto.MetaDto.builder()
                        .total(count)
                        .pageNo(pageNo)
                        .pageSize(pageSize)
                        .sortBy(sortBy)
                        .build(),
                result));

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "users",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> get(@RequestParam long id) {

        return ResponseEntity.ok(userService.getWithDetail(id));
    }

}
