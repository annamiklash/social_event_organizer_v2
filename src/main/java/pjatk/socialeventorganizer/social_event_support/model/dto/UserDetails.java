package pjatk.socialeventorganizer.social_event_support.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Embeddable
public class UserDetails implements Serializable {

    private String firstName;

    private String lastName;

    private String email;

}
