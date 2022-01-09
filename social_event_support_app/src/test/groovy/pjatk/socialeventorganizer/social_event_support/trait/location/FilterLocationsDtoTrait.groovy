package pjatk.socialeventorganizer.social_event_support.trait.location

import com.google.common.collect.ImmutableList
import pjatk.socialeventorganizer.social_event_support.enums.LocationDescriptionItemEnum
import pjatk.socialeventorganizer.social_event_support.location.model.dto.FilterLocationsDto

trait FilterLocationsDtoTrait {

    FilterLocationsDto fakeFilterLocationsDto = FilterLocationsDto.builder()
            .city('Warszawa')
            .guestCount(1)
            .isSeated(true)
            .date('2007-01-01')
            .minPrice('10')
            .maxPrice('20')
            .descriptionItems(ImmutableList.of(
                    LocationDescriptionItemEnum.CAN_BRING_OWN_ALCOHOL,
                    LocationDescriptionItemEnum.OUTSIDE_CATERING_AVAILABLE
            ))
            .build()


}