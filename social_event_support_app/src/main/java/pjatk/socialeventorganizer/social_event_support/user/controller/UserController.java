package pjatk.socialeventorganizer.social_event_support.user.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.business.mapper.BusinessMapper;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto;
import pjatk.socialeventorganizer.social_event_support.business.service.BusinessService;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.customer.mapper.CustomerMapper;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;
import pjatk.socialeventorganizer.social_event_support.table.TableDto;
import pjatk.socialeventorganizer.social_event_support.user.mapper.UserMapper;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.model.dto.*;
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

    private final SecurityService securityService;
    private final UserService userService;
    private final CustomerService customerService;
    private final BusinessService businessService;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "register/business",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessDto> businessSignup(@RequestBody @Valid BusinessUserRegistrationDto dto) {

        final Business business = businessService.createBusinessAccount(dto);
        return ResponseEntity.ok(BusinessMapper.toDtoWithUser(business));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "register/customer",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> customerSignup(@RequestBody @Valid CustomerUserRegistrationDto dto) {
        final Customer customer = customerService.createCustomerAccount(dto);
        return ResponseEntity.ok(CustomerMapper.toDto(customer));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto, HttpServletRequest request) {
        request.getSession().invalidate();

        if (!userService.isActive(loginDto)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (!securityService.isPasswordMatch(loginDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        securityService.buildSecurityContext(loginDto, request);
        final User user = userService.getUserByEmail(loginDto.getEmail());
        if ('B' == user.getType()) {
            return ResponseEntity.ok(
                    BusinessMapper.toDtoWithUser(businessService.get(user.getId())));
        }
        if ('C' == user.getType()) {
            return ResponseEntity.ok(
                    CustomerMapper.toDto(customerService.get(user.getId())));
        }
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PostMapping("/reset_password")
    public ResponseEntity<Void> sendResetPasswordEmail(HttpServletRequest request, @RequestParam String email) {
//        final String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api";
        final String appUrl = "http://localhost:3000/reset_password";
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


    @RequestMapping(
            method = RequestMethod.GET,
            path = "logout")
    ResponseEntity<Void> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok().build();
    }
}
