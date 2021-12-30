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

    private String city;

    private Integer guestCount;

    private Boolean isSeated;

    private String date;

    private String minPrice;

    private String maxPrice;

    private List<LocationDescriptionItemEnum> descriptionItems;
}
