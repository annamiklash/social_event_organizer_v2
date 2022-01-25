package pjatk.socialeventorganizer.social_event_support.catering.model;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.image.model.CateringImage;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.reviews.catering.model.CateringReview;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "catering")
@Entity(name = "catering")
public class Catering implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catering")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private BigInteger phoneNumber;

    @Column(nullable = false)
    private BigDecimal serviceCost;

    @Column(nullable = false)
    private String description;

    private double rating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    private Boolean offersOutsideCatering;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_business", nullable = false)
    private Business business;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "id_catering_address")
    private Address cateringAddress;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            mappedBy = "catering",
            orphanRemoval = true)
    private Set<CateringItem> cateringItems;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "id_catering")
    private Set<CateringBusinessHours> cateringBusinessHours;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "id_catering")
    private Set<CateringReview> reviews;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "id_catering")
    private Set<CateringImage> images;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "caterings", fetch = FetchType.LAZY)
    private Set<Location> locations = new HashSet<>();

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "id_catering")
    private Set<CateringForChosenEventLocation> cateringForChosenEventLocations;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
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
        cuisines.add(cuisine);
    }

    public void removeCuisine(Cuisine cuisine) {
        if (cuisine == null) {
            throw new IllegalArgumentException("Cuisine cannot be null");
        }
        cuisines.remove(cuisine);
    }


}
