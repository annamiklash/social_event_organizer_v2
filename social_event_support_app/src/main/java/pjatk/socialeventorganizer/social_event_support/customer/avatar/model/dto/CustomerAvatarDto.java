package pjatk.socialeventorganizer.social_event_support.customer.avatar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.CustomerDto;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerAvatarDto implements Serializable {

    private Long id;

    private String path;

    private byte[] image;

    private CustomerDto customer;
}
