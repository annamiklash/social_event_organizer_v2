package pjatk.socialeventorganizer.social_event_support.location.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.enums.LocationDescriptionItemEnum;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterLocationsDto implements Serializable {

    private List<LocationDescriptionItemEnum> descriptionItems;

    private String date;

    private String timeFrom;

    private String timeTo;

    private String country;

    private String city;

    private Integer guestCount;

    private Boolean isSeated;

}
