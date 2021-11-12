package pjatk.socialeventorganizer.social_event_support.business.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;
import pjatk.socialeventorganizer.social_event_support.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "business")
@Table(name = "business")
public class Business implements Serializable {

    @Id
    @Column(name = "id_business_user")
    Long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "business_name")
    String businessName;

    @Column(name = "phone_number")
    BigInteger phoneNumber;

    @Column(name = "verification_status")
    String verificationStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_business_address")
    Address address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_business")
    Set<Catering> caterings = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_business")
    Set<Location> locations = new HashSet<>();

    @PrimaryKeyJoinColumn
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    User user;
}
