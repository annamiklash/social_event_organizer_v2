package pjatk.socialeventorganizer.social_event_support.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Filter {

    private String field;
    private QueryOperator operator;
    private String value;
    private List<String> values; //Used in case of IN operator
}
