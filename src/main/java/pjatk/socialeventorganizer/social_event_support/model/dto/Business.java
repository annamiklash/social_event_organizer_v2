package pjatk.socialeventorganizer.social_event_support.model.dto;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "business")
@Table(name = "business")
public class Business implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_business")
    Long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "business_name")
    String businessName;

    @Column
    String email;

    @Column(name = "phone_number")
    BigInteger phoneNumber;

    @Column(name = "hashed_password")
    String password;

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


}
