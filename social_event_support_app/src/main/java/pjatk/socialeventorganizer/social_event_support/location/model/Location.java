package pjatk.socialeventorganizer.social_event_support.location.model;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.availability.location.model.LocationAvailability;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.businesshours.location.model.LocationBusinessHours;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.location.locationforevent.model.LocationForEvent;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
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
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column(name = "phone_number")
    private BigInteger phoneNumber;

    @Column(name = "seating_capacity")
    private Integer seatingCapacity;

    @Column(name = "standing_capacity")
    private Integer standingCapacity;

    @Column
    private String description;

    @Column(name = "daily_rent_cost")
    private BigDecimal dailyRentCost;

    @Column(name = "size_in_sq_meters")
    private Integer sizeSqMeters;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_business", nullable = false)
    private Business business;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location_address")
    private Address locationAddress;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "catering_location",
            joinColumns = @JoinColumn(name = "id_location"),
            inverseJoinColumns = @JoinColumn(name = "id_catering"))
    private Set<Catering> caterings = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "location_description",
            joinColumns = @JoinColumn(name = "id_location"),
            inverseJoinColumns = @JoinColumn(name = "name"))
    private Set<LocationDescriptionItem> descriptions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Set<LocationImage> images = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Set<LocationForEvent> locationForEvent = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_location")
    private Set<LocationAvailability> availability;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_location")
    private Set<LocationBusinessHours> locationBusinessHours;

    public void addAvailability(LocationAvailability locationAvailability) {
        availability.add(locationAvailability);
    }

    public void removeAvailability(LocationAvailability locationAvailability) {
        availability.remove(locationAvailability);
    }

    public void addDescriptionItem(LocationDescriptionItem locationDescriptionItem) {
        if (locationDescriptionItem == null) {
            throw new IllegalArgumentException("Location description item cannot be null");
        }

        descriptions.add(locationDescriptionItem);
    }

    public void removeDescriptionItem(LocationDescriptionItem locationDescriptionItem) {
        if (locationDescriptionItem == null) {
            throw new IllegalArgumentException("Location description item cannot be null");
        }

        descriptions.remove(locationDescriptionItem);
    }

    public void addCatering(Catering catering) {
        if (catering == null) {
            throw new IllegalArgumentException("Catering cannot be null");
        }
        caterings.add(catering);
    }

    public void removeCatering(Catering catering) {
        if (catering == null) {
            throw new IllegalArgumentException("Catering cannot be null");
        }
        caterings.remove(catering);
    }
}
