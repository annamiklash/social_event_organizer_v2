package pjatk.socialeventorganizer.social_event_support.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static pjatk.socialeventorganizer.social_event_support.security.enums.AllowedUrlsEnum.*;


@AllArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(final WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers(
                REGISTER.value,
                LOGIN.value,
                LOGOUT.value,
                LOCATIONS.value,
                LOCATIONS_REVIEW.value,
                LOCATION_AVAILABILITY.value,

                CATERINGS.value,
                CATERING_AVAILABILITY.value,
                CUISINES.value,
                CATERINGS_REVIEW.value,
                CATERING_ITEMS.value,
                CATERING_IMAGES.value,
                LOCATIONS.value,
                LOCATION_AVAILABILITY.value,
                LOCATION_IMAGES.value,
                LOGIN.value,
                LOGOUT.value,
                SERVICES.value,
                SERVICES_AVAILABILITY.value,
                SERVICE_REVIEW.value,
                SERVICE_IMAGES.value,
                REGISTER.value,
                RESET_PASSWORD.value,
                RESET.value,

                EVENT_TYPES.value
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }

}
