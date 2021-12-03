package pjatk.socialeventorganizer.social_event_support.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
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
                CATERINGS.value,
                SERVICES.value,
                CUISINES.value,
                RESET_PASSWORD.value,
                RESET.value
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }

}
