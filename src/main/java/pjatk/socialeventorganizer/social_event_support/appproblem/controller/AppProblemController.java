package pjatk.socialeventorganizer.social_event_support.appproblem.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblem;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.request.AppProblemRequest;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.response.AppProblemInformationResponse;
import pjatk.socialeventorganizer.social_event_support.appproblem.service.AppProblemService;
import pjatk.socialeventorganizer.social_event_support.catering.model.request.CateringRequest;
import pjatk.socialeventorganizer.social_event_support.catering.model.response.CateringInformationResponse;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/app_problem")
public class AppProblemController {

    private final AppProblemService service;

   @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AppProblemInformationResponse>> findAll() {
        log.info("GET ALL APP_PROBLEMS");
        return ResponseEntity.ok(service.findAll());
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/user_id/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AppProblemInformationResponse>> findByUserId(@PathVariable Integer userId) {
        log.info("GET ALL PROBLEMS FROM USER WITH ID " + userId);
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER','BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createAppProblem(@Valid @RequestBody AppProblemRequest request) {
        service.addNewAppProblem(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER','BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/{app_problem_id}", //name same as function argument
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCatering(@Valid @RequestBody AppProblemRequest request, @PathVariable Long app_problem_id) {
        try {
            service.updateAppProblem(app_problem_id, request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}")
    public ResponseEntity<Void> deleteAppProblem(@PathVariable Long id) {
        try {
            service.deleteAppProblem(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
}
