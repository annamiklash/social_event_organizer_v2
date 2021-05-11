package pjatk.socialeventorganizer.social_event_support.common.security.model;

import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserCredentials {

    String login;

    Long userId;

    Long customerId;

    Long businessId;

    Character userType;
}
