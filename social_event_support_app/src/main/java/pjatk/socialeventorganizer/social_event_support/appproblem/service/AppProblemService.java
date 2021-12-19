package pjatk.socialeventorganizer.social_event_support.appproblem.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.appproblem.AppProblem;
import pjatk.socialeventorganizer.social_event_support.appproblem.AppProblemTypeEnum;
import pjatk.socialeventorganizer.social_event_support.appproblem.mapper.AppProblemMapper;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblemDto;
import pjatk.socialeventorganizer.social_event_support.appproblem.repository.AppProblemRepository;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AppProblemService {

    private final AppProblemRepository appProblemRepository;

    private final UserService userService;

    public List<AppProblem> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageRequest.of(customPage.getFirstResult(), customPage.getMaxResult(), Sort.by(customPage.getSort()).descending());
        final Page<AppProblem> page = appProblemRepository.findAllWithKeyword(paging);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }


    public AppProblem get(long id) {
        return appProblemRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new NotFoundException("App problem with id " + id + " DOES NOT EXIST"));
    }

    public ImmutableList<AppProblem> getByUserId(long id) {
        userService.get(id);
        return ImmutableList.copyOf(appProblemRepository.findByUser_Id(id));

    }

    public AppProblem resolve(long id) {
        final AppProblem appProblem = get(id);

        appProblem.setResolvedAt(LocalDateTime.now());
        save(appProblem);

        return appProblem;
    }

    public void save(AppProblem appProblem) {
        appProblemRepository.save(appProblem);
    }

    public AppProblem create(AppProblemDto dto, long id) {
        final User user = userService.get(id);

        final Optional<AppProblemTypeEnum> optionalConcern = Arrays.stream(AppProblemTypeEnum.values()).filter(appProblemType -> appProblemType.getValue().equals(dto.getConcern())).findAny();

        if (optionalConcern.isEmpty()) {
            throw new NotFoundException("No matching concern found");
        }
        final AppProblem appProblem = AppProblemMapper.fromDto(dto);

        appProblem.setUser(user);
        appProblem.setCreatedAt(LocalDateTime.now());

        save(appProblem);

        return appProblem;
    }

}
