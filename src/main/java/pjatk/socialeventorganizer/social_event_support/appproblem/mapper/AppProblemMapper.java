package pjatk.socialeventorganizer.social_event_support.appproblem.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblem;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.request.AppProblemRequest;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.response.AppProblemInformationResponse;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.response.AppProblemResponse;

import java.time.LocalDateTime;


@Component
public class AppProblemMapper {

    public AppProblem mapToDTO(AppProblemRequest appProblemRequest, Integer userId) {

        return AppProblem.builder()
                .concern(appProblemRequest.getConcern())
                .dateTime(LocalDateTime.now())
                .description(appProblemRequest.getDescription())
                .userId(userId)
                .build();
    }

    public AppProblemResponse  mapDTOtoAppProblemResponse(AppProblem appProblem) {
        return AppProblemResponse.builder()
                .id(Math.toIntExact(appProblem.getId()))
                .concern(appProblem.getConcern())
                .build();
    }
    public void updateDTO(AppProblemRequest appProblemRequest, AppProblem fetchedAppProblem) {

        fetchedAppProblem.setConcern(appProblemRequest.getConcern());
        fetchedAppProblem.setDescription(appProblemRequest.getDescription());
        //fetchedAppProblem.setDateTime(LocalDateTime.now());
        //fetchedAppProblem.setUserId(fetchedAppProblem.getUserId());

    }

    public AppProblemInformationResponse mapDTOtoAppProblemInformationResponse(AppProblem appProblem) {
        return AppProblemInformationResponse.builder()
                .id(appProblem.getId())
                .concern(appProblem.getConcern())
                .description(appProblem.getDescription())
                .dateTime(appProblem.getDateTime())
                .userId(appProblem.getUserId())
                .build();
    }
}
