package pjatk.socialeventorganizer.social_event_support.customer.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {

    @NotNull
    private String subject;

    @NotNull
    private String content;

    private String receiverEmail;

}
