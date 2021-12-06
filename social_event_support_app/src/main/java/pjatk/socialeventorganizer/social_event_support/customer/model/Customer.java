package pjatk.socialeventorganizer.social_event_support.customer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.CustomerAvatar;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;
import pjatk.socialeventorganizer.social_event_support.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer_user")
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private BigInteger phoneNumber;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer")
    @JsonIgnore
    private Set<Guest> guests;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer")
    @JsonIgnore
    private Set<OrganizedEvent> events;

    @PrimaryKeyJoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avatar")
    private CustomerAvatar avatar;

}
