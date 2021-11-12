package pjatk.socialeventorganizer.social_event_support.common.paginator;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pageable {

    String firstResult() default "0";

    String maxResult() default "50";

    Sort sort() default @Sort(active = false);


}
