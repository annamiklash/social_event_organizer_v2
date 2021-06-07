//package pjatk.socialeventorganizer.social_event_support;//package pjatk.socialeventorganizer.SocialEventOrganizer;
//
//
//import com.google.common.collect.ImmutableList;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDescriptionItem;
//import pjatk.socialeventorganizer.social_event_support.location.model.response.LocationInformationResponse;
//import pjatk.socialeventorganizer.social_event_support.location.service.LocationDescriptionItemService;
//import pjatk.socialeventorganizer.social_event_support.location.service.LocationService;
//
//
//@Slf4j
//@SpringBootApplication
//@AllArgsConstructor
//public class SocialEventOrganizerApplicationConsoleRunner implements CommandLineRunner {
//
//    private final LocationService service;
//
//    public static void main(String[] args) {
//        SpringApplication.run(SocialEventOrganizerApplicationConsoleRunner.class, args);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        final ImmutableList<LocationInformationResponse> all = service.findAll();
//        log.info(byId.toString());
//
//    }
//}
