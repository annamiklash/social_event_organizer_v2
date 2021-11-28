package pjatk.socialeventorganizer.social_event_support.optional_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterOptionalServiceDto {

    private String date;

    private String timeFrom;

    private String timeTo;

    private String priceNotLessThen;

    private String priceNotMoreThan;

    private String type;

    private List<String> musicStyles;

    private Integer ageFrom;

    private Integer ageTo;

    private Integer bandPeopleCount;

    private List<String> languages;

    private String keyword;
}
