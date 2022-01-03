package pjatk.socialeventorganizer.social_event_support.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.common.constants.RegexConstants;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.dto.GuestDto;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto.LocationForEventDto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizedEventDto implements Serializable {

    private Long id;

    @Size(min = 1, max = 100, message = "Cannot be more that 100 symbols")
    @NotBlank(message = "Event name is mandatory")
    private String name;

    @NotBlank(message = "Date is mandatory")
    @Pattern(regexp = RegexConstants.DATE_REGEX)
    @JsonFormat(pattern = DateTimeUtil.DATE_FORMAT)
    private String date;

    @NotBlank(message = "Start time is mandatory")
    @Pattern(regexp = RegexConstants.TIME_REGEX)
    @JsonFormat(pattern = DateTimeUtil.TIME_FORMAT)
    private String startTime;

    @NotBlank(message = "End time is mandatory")
    @Pattern(regexp = RegexConstants.TIME_REGEX)
    @JsonFormat(pattern = DateTimeUtil.TIME_FORMAT)
    private String endTime;

    @Min(1)
    @NotNull(message = "Guest amount is mandatory")
    private Integer guestCount;

    @NotBlank(message = "Event type is mandatory")
    private String eventType;

    private String eventStatus;

    private String createdAt;

    private String modifiedAt;

    private String deletedAt;

    private CustomerDto customer;

    private List<GuestDto> guests;

    private LocationForEventDto location;

}
