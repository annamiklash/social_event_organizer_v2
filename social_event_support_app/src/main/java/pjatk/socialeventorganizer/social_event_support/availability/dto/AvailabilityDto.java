package pjatk.socialeventorganizer.social_event_support.availability.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto;
import pjatk.socialeventorganizer.social_event_support.common.constants.RegexConstants;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailabilityDto implements Serializable {

    private Long id;

    @NotBlank(message = "Date is mandatory")
    @Pattern(regexp = RegexConstants.DATE_REGEX)
    @JsonFormat(pattern = DateTimeUtil.DATE_FORMAT)
    private String date;

    @NotBlank(message = "Time from is mandatory")
    @Pattern(regexp = RegexConstants.TIME_REGEX_WITH_SECONDS)
    @JsonFormat(pattern = DateTimeUtil.TIME_FORMAT_WITH_SECONDS)
    private String timeFrom;

    @NotBlank(message = "Time to is mandatory")
    @Pattern(regexp = RegexConstants.TIME_REGEX_WITH_SECONDS)
    @JsonFormat(pattern = DateTimeUtil.TIME_FORMAT_WITH_SECONDS)
    private String timeTo;

    private String status;

    private LocationDto location;

    private CateringDto catering;

    private OptionalServiceDto optionalService;

}
