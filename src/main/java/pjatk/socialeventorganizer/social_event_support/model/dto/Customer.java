package pjatk.socialeventorganizer.social_event_support.model.dto;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "customer")
@Entity(name = "customer")
public class Customer extends User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    Long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column
    LocalDate birthdate;

    @Column(name = "phone_number")
    BigInteger phoneNumber;

    @Column(name = "hashed_password")
    String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_customer_address")
    Address address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_customer")
    Set<Guest> guests = new HashSet<>();


}
