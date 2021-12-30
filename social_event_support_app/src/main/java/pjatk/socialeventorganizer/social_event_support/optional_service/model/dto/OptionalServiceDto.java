package pjatk.socialeventorganizer.social_event_support.optional_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto;
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto;
import pjatk.socialeventorganizer.social_event_support.common.constants.RegexConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionalServiceDto implements Serializable {

    private long id;

    @NotNull
    private String alias;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 1, max = 30, message
            = "The name should be between 1 and 30 characters")
    @Pattern(regexp = RegexConstants.FIRST_NAME_REGEX)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 1, max = 40, message
            = "The name should be between 1 and 40 characters")
    @Pattern(regexp = RegexConstants.LAST_NAME_REGEX)
    private String lastName;

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

    @NotNull
    private AddressDto address;

    private Set<MusicStyleDto> musicStyle;

    private String instrument;

    private Set<TranslationLanguageDto> translationLanguages;

    private List<AvailabilityDto> serviceAvailability;

    private Integer musicBandPeopleCount;

    private String kidPerformerType;

    private Integer kidAgeFrom;

    private Integer kidAgeTo;

    private String createdAt;

    private String modifiedAt;

    private String deletedAt;

    private BusinessDto business;
}
