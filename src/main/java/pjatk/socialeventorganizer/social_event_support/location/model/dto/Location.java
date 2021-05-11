package pjatk.socialeventorganizer.social_event_support.location.model.dto;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.Catering;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;

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
@Table(name = "location")
@Entity(name = "location")
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location")
    Long id;

    @Column
    String name;

    @Column
    String email;

    @Column(name = "phone_number")
    BigInteger phoneNumber;

    @Column(name = "seating_capacity")
    Integer seatingCapacity;

    @Column(name = "standing_capacity")
    Integer standingCapacity;

    @Column
    String description;

    @Column(name = "daily_rent_cost")
    BigDecimal dailyRentCost;

    @Column(name = "size_in_sq_meters")
    Integer sizeSqMeters;

    @Column(name = "id_business_user")
    Integer businessId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_location_address")
    Address locationAddress;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "catering_location",
            joinColumns = @JoinColumn(name = "id_location"),
            inverseJoinColumns = @JoinColumn(name = "id_catering"))
    Set<Catering> caterings = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "location_description",
            joinColumns = @JoinColumn(name = "id_location"),
            inverseJoinColumns = @JoinColumn(name = "name"))
    Set<LocationDescriptionItem> descriptions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_location")
    Set<LocationImage> images = new HashSet<>();


    public void addCatering(Catering catering) {
        if (catering == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        caterings.add(catering);
    }

    public void removeCatering(Catering catering) {
        if (catering == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        caterings.remove(catering);
    }
}
