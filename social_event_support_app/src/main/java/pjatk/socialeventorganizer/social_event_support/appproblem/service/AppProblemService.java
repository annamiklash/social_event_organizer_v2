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
import pjatk.socialeventorganizer.social_event_support.appproblem.mapper.AppProblemMapper;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.AppProblem;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblemDto;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.enums.AppProblemStatusEnum;
import pjatk.socialeventorganizer.social_event_support.appproblem.repository.AppProblemRepository;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.common.util.TimestampHelper;
import pjatk.socialeventorganizer.social_event_support.enums.AppProblemTypeEnum;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.user.model.User;
import pjatk.socialeventorganizer.social_event_support.user.service.UserService;

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

    private final TimestampHelper timestampHelper;

    public List<AppProblem> list(CustomPage customPage, String keyword, AppProblemStatusEnum status) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();
        final Pageable paging = PageRequest.of(customPage.getPageNo(), customPage.getPageSize(), Sort.by(customPage.getSortBy()).descending());

        if (status == AppProblemStatusEnum.RESOLVED) {
            final Page<AppProblem> page = appProblemRepository.findAllWithKeywordResolved(paging);
            return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
        }
        if (status == AppProblemStatusEnum.NOT_RESOLVED) {
            final Page<AppProblem> page = appProblemRepository.findAllWithKeywordNotResolved(paging);
            return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
        }

        final Page<AppProblem> page = appProblemRepository.findAllWithKeyword(paging);
        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }


    public AppProblem get(long id) {
        return appProblemRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new NotFoundException("App problem with id " + id + " DOES NOT EXIST"));
    }

    public ImmutableList<AppProblem> getByUserId(long id) {
        return ImmutableList.copyOf(appProblemRepository.findByUser_Id(id));

    }

    public AppProblem resolve(long id) {
        final AppProblem appProblem = get(id);

        appProblem.setResolvedAt(timestampHelper.now());
        save(appProblem);

        return appProblem;
    }

    public AppProblem create(AppProblemDto dto, long id) {
        final User user = userService.get(id);

        final Optional<AppProblemTypeEnum> optionalConcern = Arrays.stream(
                AppProblemTypeEnum.values())
                .filter(appProblemType -> appProblemType.getValue().equals(dto.getConcern()))
                .findAny();

        if (optionalConcern.isEmpty()) {
            throw new NotFoundException("No matching concern found");
        }
        final AppProblem appProblem = AppProblemMapper.fromDto(dto);

        appProblem.setUser(user);
        appProblem.setCreatedAt(timestampHelper.now());

        save(appProblem);

        return appProblem;
    }

    private void save(AppProblem appProblem) {
        appProblemRepository.save(appProblem);
    }

}
