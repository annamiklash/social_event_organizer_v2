package pjatk.socialeventorganizer.social_event_support.appproblem.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblem;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.request.AppProblemRequest;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.response.AppProblemInformationResponse;
import pjatk.socialeventorganizer.social_event_support.appproblem.service.AppProblemService;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/app_problem")
public class AppProblemController {

    private final AppProblemService service;

   // @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AppProblemInformationResponse>> findAll() {
        log.info("GET ALL APP_PROBLEMS");
//        final ImmutableList<AppProblemInformationResponse> all = service.findAll();
//        log.info(String.valueOf(all.size()));
        return ResponseEntity.ok(service.findAll());
    }

    //@PreAuthorize("hasAnyAuthority('ADMIN','NEW_USER')")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createAppProblem(@Valid @RequestBody AppProblemRequest request) {
        service.addNewAppProblem(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
