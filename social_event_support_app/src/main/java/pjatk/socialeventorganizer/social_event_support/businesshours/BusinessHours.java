package pjatk.socialeventorganizer.social_event_support.businesshours;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalTime;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public abstract class BusinessHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_business_hours")
    private long id;

    private String day;

    private LocalTime timeFrom;

    private LocalTime timeTo;


}
