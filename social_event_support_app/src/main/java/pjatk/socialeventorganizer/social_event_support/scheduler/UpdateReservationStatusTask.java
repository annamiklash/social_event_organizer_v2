package pjatk.socialeventorganizer.social_event_support.scheduler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.event.service.OrganizedEventService;

@Component
@Data
@AllArgsConstructor
@Log4j2
public class UpdateReservationStatusTask {

    private final OrganizedEventService organizedEventService;

//    @Scheduled( cron = "0 0/2 * * * ?") //every 2 min
    @Scheduled( cron = "1 0 * * * ?")
    public void scheduleFixedRateTaskAsync() {
        organizedEventService.performUpdate();
        log.info("UPDATED");
    }
}
