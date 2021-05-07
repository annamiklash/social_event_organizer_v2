package pjatk.socialeventorganizer.social_event_support.model.dto;

import lombok.*;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address")
    private Long id;

    @Column
    String country;

    @Column
    String city;

    @Column(name = "street_name")
    String streetName;

    @Column(name = "street_number")
    int streetNumber;

    @Column(name = "zip_code")
    String zipCode;



}
