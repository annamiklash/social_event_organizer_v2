package pjatk.socialeventorganizer.social_event_support.address.model;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.common.SortField;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "address")
@Table(name = "address")
public class Address implements Serializable {

    public static class Sort {
        public static final SortField CITY = SortField.unique("city");
        public static final SortField COUNTRY = SortField.unique("country");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address")
    private Long id;

    @Column
    private String country;

    @Column
    private String city;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "street_number")
    private int streetNumber;

    @Column(name = "zip_code")
    private String zipCode;


}