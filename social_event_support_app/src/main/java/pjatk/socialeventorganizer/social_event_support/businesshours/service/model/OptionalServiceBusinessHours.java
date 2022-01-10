package pjatk.socialeventorganizer.social_event_support.businesshours.service.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.businesshours.BusinessHours;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "optional_service_business_hours")
@Table(name = "optional_service_business_hours")
public class OptionalServiceBusinessHours extends BusinessHours {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private OptionalService optionalService;
}
