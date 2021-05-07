package pjatk.socialeventorganizer.social_event_support;//package pjatk.socialeventorganizer.SocialEventOrganizer;
//
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import pjatk.socialeventorganizer.SocialEventOrganizer.model.dto.LocationDescriptionItem;
//import pjatk.socialeventorganizer.SocialEventOrganizer.service.LocationDescriptionItemService;
//
//@Slf4j
//@SpringBootApplication
//@AllArgsConstructor
//public class SocialEventOrganizerApplicationConsoleRunner implements CommandLineRunner {
//
//    private final LocationDescriptionItemService service;
//
//    public static void main(String[] args) {
//        SpringApplication.run(SocialEventOrganizerApplicationConsoleRunner.class, args);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        final LocationDescriptionItem byId = service.getById("Has Wi-Fi");
//        log.info(byId.toString());
//
//    }
//}
