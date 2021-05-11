package pjatk.socialeventorganizer.social_event_support.customer.model.dto;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.customer.guest.Guest;
import pjatk.socialeventorganizer.social_event_support.user.model.User;

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
public class Customer implements Serializable {

    @Id
    @Column(name = "id_customer_user")
    Long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column
    LocalDate birthdate;

    @Column(name = "phone_number")
    BigInteger phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_customer_address")
    Address address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_customer_user")
    Set<Guest> guests = new HashSet<>();

    @PrimaryKeyJoinColumn
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    User user;


}
