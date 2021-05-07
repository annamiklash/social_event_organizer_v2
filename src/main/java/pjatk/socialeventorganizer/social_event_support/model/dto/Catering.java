package pjatk.socialeventorganizer.social_event_support.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pjatk.socialeventorganizer.social_event_support.model.exception.IllegalArgumentException;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
/*
NAMING AS IN DB (tables and attributes)
 or with annotation @Column(name="column_name)/@Entity(name="table_name)
 */
@Table(name = "catering")
@Entity(name = "catering")
public class Catering implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catering")
    Long id;

    @Column
    String name;

    @Column
    String email;

    @Column(name = "phone_number")
    BigInteger phoneNumber;

    @Column(name = "service_cost")
    BigDecimal serviceCost;

    @Column
    String description;

    @Column(name = "id_business")
    Integer businessId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_catering_address")
    Address cateringAddress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_catering")
    Set<CateringItem> cateringItems = new HashSet<>();

    @ManyToMany(mappedBy = "caterings", fetch = FetchType.LAZY)
    @JsonIgnore
    Set<Location> locations = new HashSet<>();

    public void addLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        locations.add(location);
    }

    public void removeLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        locations.remove(location);
    }
}
