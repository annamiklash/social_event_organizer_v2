package pjatk.socialeventorganizer.social_event_support.optional_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.model.OptionalServiceBusinessHours;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.music.musicstyle.MusicStyle;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "optional_service")
@Entity(name = "optional_service")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type",
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

    @Column
    private String alias;

    @Column(insertable = false, updatable = false, nullable = false)
    private String type;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String description;

    @Column(name = "service_cost", nullable = false)
    private BigDecimal serviceCost;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_business", nullable = false)
    private Business business;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_optional_service")
    private Set<OptionalServiceBusinessHours> optionalServiceBusinessHours;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_optional_service")
    private Set<OptionalServiceAvailability> availability;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "service_music_style",
            joinColumns = @JoinColumn(name = "id_optional_service"),
            inverseJoinColumns = @JoinColumn(name = "id_music_style"))
    private Set<MusicStyle> styles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private Set<OptionalServiceForChosenLocation> serviceForLocation;

    public void addMusicStyle(MusicStyle musicStyle) {
        styles.add(musicStyle);
    }

    public void removeMusicStyle(MusicStyle musicStyle) {
        styles.remove(musicStyle);
    }

}
