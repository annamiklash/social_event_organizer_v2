package pjatk.socialeventorganizer.social_event_support.optional_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.model.OptionalServiceBusinessHours;
import pjatk.socialeventorganizer.social_event_support.image.model.OptionalServiceImage;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.music.musicstyle.MusicStyle;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;
import pjatk.socialeventorganizer.social_event_support.reviews.service.model.OptionalServiceReview;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "optional_service")
@Entity(name = "optional_service")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "d_type",
        discriminatorType = DiscriminatorType.STRING)
public class OptionalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_optional_service")
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String alias;

    @Column(name = "d_type", nullable = false,
            insertable = false, updatable = false)
    private String dType;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private BigDecimal serviceCost;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_business", nullable = false)
    private Business business;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_service_address")
    private Address serviceAddress;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private Set<OptionalServiceBusinessHours> optionalServiceBusinessHours;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private Set<OptionalServiceReview> reviews;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private Set<OptionalServiceAvailability> availability;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private Set<OptionalServiceImage> images;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "service_music_style",
            joinColumns = @JoinColumn(name = "id_optional_service"),
            inverseJoinColumns = @JoinColumn(name = "id_music_style"))
    private Set<MusicStyle> styles;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "id_optional_service")
    private Set<OptionalServiceForChosenLocation> serviceForLocation;

    public void addMusicStyle(MusicStyle musicStyle) {
        styles.add(musicStyle);
    }

    public void removeMusicStyle(MusicStyle musicStyle) {
        styles.remove(musicStyle);
    }



}
