package pjatk.socialeventorganizer.social_event_support.businesshours.catering.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import net.logstash.logback.encoder.org.apache.commons.lang3.builder.HashCodeExclude;
import pjatk.socialeventorganizer.social_event_support.businesshours.BusinessHours;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "catering_business_hours")
@Table(name = "catering_business_hours")
public class CateringBusinessHours extends BusinessHours {

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering")
    private Catering catering;

}