package pjatk.socialeventorganizer.social_event_support.image.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {

    private long id;

    @NotNull
    private String path;

    @NotNull
    private boolean isMain;

    private String type;

    private long size;

    private byte[] image;

    private LocationDto location;

    private CateringDto catering;

    private OptionalServiceDto service;

}
