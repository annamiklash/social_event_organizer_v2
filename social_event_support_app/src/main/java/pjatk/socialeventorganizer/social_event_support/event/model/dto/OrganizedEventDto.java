package pjatk.socialeventorganizer.social_event_support.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.dto.LocationForEventDto;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizedEventDto implements Serializable {


    ///TODO: refactor
    private Long id;

    private String name;

    private String startDate;

    private String endDate;

    private Boolean isPredefined;

    private String eventStatus;

    private EventTypeDto eventType;

    private String createdAt;

    private String modifiedAt;

    private String deletedAt;

    private CustomerDto customer;

    private List<LocationForEventDto> locations;

}
