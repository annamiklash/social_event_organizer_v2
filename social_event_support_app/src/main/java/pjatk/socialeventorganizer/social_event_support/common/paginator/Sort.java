package pjatk.socialeventorganizer.social_event_support.common.paginator;

import org.apache.logging.log4j.util.Strings;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sort {

    boolean active() default true;

    String field() default Strings.EMPTY;

    String order() default "asc";
}
