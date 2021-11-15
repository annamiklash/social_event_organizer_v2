package pjatk.socialeventorganizer.social_event_support.appproblem.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.socialeventorganizer.social_event_support.appproblem.AppProblem;
import pjatk.socialeventorganizer.social_event_support.appproblem.AppProblemTypeEnum;
import pjatk.socialeventorganizer.social_event_support.appproblem.mapper.AppProblemMapper;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblemDto;
import pjatk.socialeventorganizer.social_event_support.appproblem.service.AppProblemService;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/problems")
public class AppProblemController {

    private AppProblemService appProblemService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AppProblemDto>> list(@RequestParam(required = false) String keyword,
                                                             @RequestParam(defaultValue = "0") Integer firstResult,
                                                             @RequestParam(defaultValue = "50") Integer maxResult,
                                                             @RequestParam(defaultValue = "id") String sort,
                                                             @RequestParam(defaultValue = "desc") String order) {
        log.info("GET ALL APP_PROBLEMS");
        List<AppProblem> list = appProblemService.list(new CustomPage(maxResult, firstResult, sort, order), keyword);
        return ResponseEntity.ok(
                ImmutableList.copyOf(list.stream()
                        .map(AppProblemMapper::toDtoWithUser)
                        .collect(Collectors.toList())));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            params = {"id"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppProblemDto> get(@RequestParam long id) {
        log.info("GET " + id);
        final AppProblem appProblem = appProblemService.get(id);

        return ResponseEntity.ok(AppProblemMapper.toDtoWithUser(appProblem));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/types",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AppProblemTypeEnum>> concerns() {

        return ResponseEntity.ok(ImmutableList.copyOf(List.of(AppProblemTypeEnum.values())));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.PUT,
            params = {"id"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppProblemDto> resolve(@RequestParam long id) {
        log.info("GET " + id);
        final AppProblem appProblem = appProblemService.resolve(id);

        return ResponseEntity.ok(AppProblemMapper.toDto(appProblem));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppProblemDto> reportProblem(@Valid @RequestBody AppProblemDto dto, @PathVariable long id) {

        final AppProblem appProblem = appProblemService.create(dto, id);

        return ResponseEntity.ok(AppProblemMapper.toDtoWithUser(appProblem));
    }


}
