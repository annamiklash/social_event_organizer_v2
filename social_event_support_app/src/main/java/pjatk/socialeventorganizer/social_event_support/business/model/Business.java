package pjatk.socialeventorganizer.social_event_support.business.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "business")
@Table(name = "business")
public class Business implements Serializable {

    @Id
    @Column(name = "id_business_user", nullable = false)
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "phone_number", nullable = false)
    private BigInteger phoneNumber;

    @Column(name = "verification_status", nullable = false)
    private String verificationStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_business_address")
    private Address address;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_business")
    private Set<Catering> caterings;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_business")
    private Set<Location> locations;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_business")
    private Set<OptionalService> services;

    @PrimaryKeyJoinColumn
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private User user;
}
