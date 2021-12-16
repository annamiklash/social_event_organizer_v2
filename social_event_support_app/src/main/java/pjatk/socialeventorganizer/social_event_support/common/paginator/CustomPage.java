package pjatk.socialeventorganizer.social_event_support.common.paginator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Builder
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor

public class CustomPage implements Serializable {


    private Integer maxResult;


    private Integer firstResult;


    private String sortBy;


    private String order;


    private Integer pageNo;

    private Integer pageSize;





}

