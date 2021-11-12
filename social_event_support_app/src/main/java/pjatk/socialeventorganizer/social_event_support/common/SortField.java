package pjatk.socialeventorganizer.social_event_support.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortField {

    private String unique;

    private String nonUnique;

    public static SortField unique(String unique) {
        SortField sortField = new SortField();
        sortField.setUnique(unique);
        return sortField;
    }

    public static SortField nonUnique(String nonUnique, String unique) {
        SortField sortField = new SortField();
        sortField.setNonUnique(nonUnique);
        sortField.setUnique(unique);
        return sortField;
    }
}
