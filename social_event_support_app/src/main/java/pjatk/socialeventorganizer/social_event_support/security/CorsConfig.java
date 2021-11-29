//package pjatk.socialeventorganizer.social_event_support.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig {
//
//    private final long MAX_AGE_SECS = 3600;
//
//    /**
//     * Disabling CORS
//     * Solution from: https://stackoverflow.com/questions/44697883/can-you-completely-disable-cors-support-in-spring
//     */
////    @Bean
////    @SuppressWarnings("deprecation")
////    public WebMvcConfigurer corsConfigurer() {
////        return new WebMvcConfigurerAdapter() {
////            @Override
////            public void addCorsMappings(CorsRegistry registry) {
////                registry.addMapping("/**")
////                        .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
////                        .allowedOrigins("http://localhost:8080", "http://loca");
////                ;
////            }
////        };
////    }
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry
//                        .addMapping("/**")
//                        .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS")
//                        .allowedOrigins("http://localhost:8080", "http://localhost:3000")
//                        .exposedHeaders("*")
//                        .allowedHeaders("*")
//                        .allowCredentials(true)
//                        .maxAge(MAX_AGE_SECS);
//            }
//        };
//    }
//}
