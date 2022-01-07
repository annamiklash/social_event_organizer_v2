package pjatk.socialeventorganizer.social_event_support.customer.avatar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.image.model.Image;

import javax.persistence.*;
import java.io.Serializable;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "customer_avatar")
@Entity(name = "customer_avatar")
public class CustomerAvatar extends Image implements Serializable {

    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private Customer customer;

}
