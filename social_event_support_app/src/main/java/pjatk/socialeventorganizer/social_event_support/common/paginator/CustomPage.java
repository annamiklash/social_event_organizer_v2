package pjatk.socialeventorganizer.social_event_support.common.paginator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class CustomPage implements Serializable {

    @NotNull
    private Integer maxResult;

    @NotNull
    private Integer firstResult;

    @NotNull
    private String sort;

    @NotNull
    private String order;



}

