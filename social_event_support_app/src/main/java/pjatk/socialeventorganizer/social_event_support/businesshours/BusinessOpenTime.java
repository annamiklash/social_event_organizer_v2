package pjatk.socialeventorganizer.social_event_support.businesshours;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalTime;

@Entity(name = "business_open_time")
public class BusinessOpenTime {

    @Id
    private long id;

    private LocalTime timeFrom;

    private LocalTime timeTo;
}
