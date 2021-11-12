package pjatk.socialeventorganizer.social_event_support.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizedEventDto implements Serializable {

    private Long id;

    private String name;

    private String startDate;

    private String endDate;

    private Boolean isPredefined;

    private String eventStatus;

    private EventTypeDto eventType;

    private CustomerDto customer;

    private String createdAt;

    private String modifiedAt;

    private String deletedAt;

//    private Set<LocationForEvent> locationsForEvent;

}
