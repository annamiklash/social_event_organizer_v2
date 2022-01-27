package pjatk.socialeventorganizer.social_event_support.trait.location


import pjatk.socialeventorganizer.social_event_support.location.model.dto.FilterLocationsDto

trait FilterLocationsDtoTrait {

    FilterLocationsDto fakeFilterLocationsDto = FilterLocationsDto.builder()
            .city('Warszawa')
            .guestCount(1)
            .isSeated(true)
            .date('2007-01-01')
            .minPrice('10')
            .maxPrice('20')
            .descriptionItems(List.of('Can bring own alcohol', 'Outside catering available'))
            .build()


}