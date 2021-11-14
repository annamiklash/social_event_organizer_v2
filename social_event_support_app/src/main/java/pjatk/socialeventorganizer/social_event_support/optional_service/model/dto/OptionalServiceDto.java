package pjatk.socialeventorganizer.social_event_support.optional_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionalServiceDto implements Serializable {

    private long id;

    @NotNull
    private String alias;

    @NotNull
    private String type;

    @NotNull
    private String email;

    @NotNull
    private String description;

    @NotNull
    private String serviceCost;

    private String createdAt;

    private String modifiedAt;

    private String deletedAt;

    private BusinessDto business;
}
