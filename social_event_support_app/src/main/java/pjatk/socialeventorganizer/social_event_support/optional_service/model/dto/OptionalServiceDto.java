package pjatk.socialeventorganizer.social_event_support.optional_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto;
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringItemDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

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

    @NotNull
    private List<BusinessHoursDto> businessHours;

    private String createdAt;

    private String modifiedAt;

    private String deletedAt;

    private List<CateringItemDto> cateringItems;

    private BusinessDto business;
}
