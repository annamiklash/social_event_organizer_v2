package pjatk.socialeventorganizer.social_event_support.catering.model;

import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.image.model.CateringImage;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@EqualsAndHashCode(exclude={"locations", "cuisines", "cateringForChosenEventLocations"})
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "catering")
@Entity(name = "catering")
public class Catering implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catering")
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column(name = "phone_number")
    private BigInteger phoneNumber;

    @Column(name = "service_cost")
    private BigDecimal serviceCost;

    @Column
    private String description;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_business", nullable = false)
    private Business business;

    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering_address")
    private Address cateringAddress;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering")
    private Set<CateringItem> cateringItems;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering")
    private Set<CateringBusinessHours> cateringBusinessHours;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering")
    private Set<CateringImage> images;

    @ToString.Exclude
    @ManyToMany(mappedBy = "caterings", fetch = FetchType.LAZY)
    private Set<Location> locations;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering")
    private Set<CateringForChosenEventLocation> cateringForChosenEventLocations;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "catering_cuisine",
            joinColumns = @JoinColumn(name = "id_catering"),
            inverseJoinColumns = @JoinColumn(name = "id_cuisine"))
    private Set<Cuisine> cuisines;


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

    public void addCuisine(Cuisine cuisine) {
        if (cuisine == null) {
            throw new IllegalArgumentException("Cuisine cannot be null");
        }
        cuisines.add(cuisine);
    }

    public void removeCuisine(Cuisine cuisine) {
        if (cuisine == null) {
            throw new IllegalArgumentException("Cuisine cannot be null");
        }
        cuisines.remove(cuisine);
    }



}
