package pjatk.socialeventorganizer.social_event_support.user.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "admins")
@Entity(name = "admins")
@PrimaryKeyJoinColumn(name = "id_admin_user")
public class Admin extends User implements Serializable {

    private String access;


}
