package pjatk.socialeventorganizer.social_event_support.catering.model;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.availability.catering.model.CateringAvailability;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours;
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static pjatk.socialeventorganizer.social_event_support.common.constants.Const.CATERING_ITEMS_MINIMUM;

@Builder
@Getter
@Setter
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

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_business", nullable = false)
    private Business business;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering_address")
    private Address cateringAddress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering")
    private Set<CateringItem> cateringItems;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_catering")
    private Set<CateringBusinessHours> cateringBusinessHours;

    @ManyToMany(mappedBy = "caterings", fetch = FetchType.LAZY)
    private Set<Location> locations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_catering")
    private Set<CateringAvailability> availability;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "catering_cuisine",
            joinColumns = @JoinColumn(name = "id_catering"),
            inverseJoinColumns = @JoinColumn(name = "id_cuisine"))
    private Set<Cuisine> cuisines;

    public void addCateringItem(CateringItem cateringItem) {
        if (cateringItem == null) {
            throw new IllegalArgumentException("CateringItem cannot be null");
        }
        cateringItems.add(cateringItem);
    }

    public void removeCateringItem(CateringItem cateringItem) {
        if (cateringItem == null) {
            throw new IllegalArgumentException("CateringItem cannot be null");
        }
        cateringItems.remove(cateringItem);
    }

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

    @Transient
    public boolean isOperating() {
        return cateringItems.size() > CATERING_ITEMS_MINIMUM;
    }
}
