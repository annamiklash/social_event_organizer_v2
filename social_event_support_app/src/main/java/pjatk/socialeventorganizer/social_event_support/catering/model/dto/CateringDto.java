package pjatk.socialeventorganizer.social_event_support.catering.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.AddressDto;
import pjatk.socialeventorganizer.social_event_support.availability.dto.AvailabilityDto;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto;
import pjatk.socialeventorganizer.social_event_support.businesshours.dto.BusinessHoursDto;
import pjatk.socialeventorganizer.social_event_support.common.constants.RegexConstants;
import pjatk.socialeventorganizer.social_event_support.cuisine.model.dto.CuisineDto;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringDto implements Serializable {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 100, message
            = "The name should be between 1 and 100 characters")
    @Pattern(regexp = RegexConstants.NAME_REGEX)
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(min = 5, max = 100, message
            = "Email should be between 5 and 100 characters")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Size(min = 9, max = 9, message
            = "phone number should be 9 characters long")
    @Pattern(regexp = RegexConstants.PHONE_NUMBER_REGEX, message = "should contain only digits")
    private String phoneNumber;

    @NotBlank(message = "If there no are service cost, please enter 0")
    @Pattern(regexp = RegexConstants.PRICE_REGEX, message = "should contain only digits or digits separated by a dot sign (1.23)")
    private String serviceCost;

    @NotNull
    private String description;

    @NotNull
    private boolean offersOutsideCatering;

    @NotNull
    private List<BusinessHoursDto> businessHours;

    @NotNull
    private List<CuisineDto> cuisines;

    private List<AvailabilityDto> availability;

    private String createdAt;

    private String modifiedAt;

    private String deletedAt;

    private AddressDto address;

    private Boolean isOperating;

    private List<CateringItemDto> cateringItems;

    private BusinessDto business;

    private List<LocationDto> locations;
}