package pjatk.socialeventorganizer.social_event_support.location.service

import com.google.common.collect.ImmutableList
import org.springframework.data.domain.PageImpl
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService
import pjatk.socialeventorganizer.social_event_support.availability.location.repository.LocationAvailabilityRepository
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository
import pjatk.socialeventorganizer.social_event_support.businesshours.location.service.LocationBusinessHoursService
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringRepository
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil
import pjatk.socialeventorganizer.social_event_support.image.repository.LocationImageRepository
import pjatk.socialeventorganizer.social_event_support.location.model.LocationDescriptionItem
import pjatk.socialeventorganizer.social_event_support.location.model.dto.FilterLocationsDto
import pjatk.socialeventorganizer.social_event_support.location.repository.LocationRepository
import pjatk.socialeventorganizer.social_event_support.reviews.location.service.LocationReviewService
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService
import pjatk.socialeventorganizer.social_event_support.trait.BusinessHoursTrait
import pjatk.socialeventorganizer.social_event_support.trait.address.AddressTrait
import pjatk.socialeventorganizer.social_event_support.trait.business.BusinessTrait
import pjatk.socialeventorganizer.social_event_support.trait.catering.CateringTrait
import pjatk.socialeventorganizer.social_event_support.trait.location.LocationTrait
import pjatk.socialeventorganizer.social_event_support.trait.page.PageTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class LocationServiceTest extends Specification implements LocationTrait,
        AddressTrait,
        CateringTrait,
        BusinessTrait,
        BusinessHoursTrait,
        PageTrait {

    @Subject
    LocationService locationService

    LocationRepository locationRepository
    LocationDescriptionItemService locationDescriptionItemService
    CateringRepository cateringRepository
    AddressService addressService
    BusinessRepository businessRepository
    LocationAvailabilityRepository locationAvailabilityRepository
    LocationBusinessHoursService locationBusinessHoursService
    SecurityService securityService
    LocationReviewService locationReviewService
    LocationImageRepository locationImageRepository
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {
        locationRepository = Mock()
        locationDescriptionItemService = Mock()
        cateringRepository = Mock()
        addressService = Mock()
        businessRepository = Mock()
        locationAvailabilityRepository = Mock()
        locationBusinessHoursService = Mock()
        securityService = Mock()
        locationReviewService = Mock()
        locationImageRepository = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        locationService = new LocationService(locationRepository,
                locationDescriptionItemService,
                cateringRepository,
                addressService,
                businessRepository,
                locationAvailabilityRepository,
                locationBusinessHoursService,
                securityService,
                locationReviewService,
                locationImageRepository,
                timestampHelper)
    }

    def "List"() {
        given:
        def customPage = fakePage
        def keyword = 'no'

        def paging = fakePaging
        def page = new PageImpl<>([fakeFullLocation])

        def target = ImmutableList.of(fakeFullLocation)

        when:
        def result = locationService.list(customPage, keyword)

        then:
        1 * locationRepository.findAllWithKeyword(paging, keyword) >> page

        result == target
    }

    def "Count"() {
        given:
        def keyword = '123'

        def target = 123L

        when:
        def result = locationService.count(keyword)

        then:
        1 * locationRepository.countAll(keyword) >> target

        result == target
    }

    def "Get"() {
        given:
        def id = 1L
        def location = fakeFullLocation

        def target = location
        when:
        def result = locationService.get(id)

        then:
        1 * locationRepository.findById(id) >> Optional.of(location)

        result == target
    }


    def "GetWithDetail"() {
        given:
        def id = 1L
        def location = fakeFullLocation
        def rating = 4
        location.setRating(rating)

        def target = location
        when:
        def result = locationService.getWithDetail(id)

        then:
        1 * locationRepository.getByIdWithDetail(id) >> Optional.of(location)
        1 * locationReviewService.getRating(id) >> rating

        result == target
    }

    def "IsAvailable"() {
        given:
        def id = 1l
        def date = '2022-02-01'
        def timeFrom = '10:00'
        def timeTo = '18:00'

        def dateTimeFrom = DateTimeUtil.joinDateAndTime(date, timeFrom);
        def dateTimeTo = DateTimeUtil.joinDateAndTime(date, timeTo);

        def location = fakeFullLocationWithAvailability

        when:
        def result = locationService.isAvailable(id, date, timeFrom, timeTo)

        then:
        1 * locationRepository.available(id, date, dateTimeFrom, dateTimeTo) >> Optional.of(location)

        result

    }

    def "GetCities"() {
        given:
        def cities = ['Warsaw, Poland']
        def target = cities

        when:
        def result = locationService.getCities()

        then:
        1 * locationRepository.findDistinctCities() >> cities

        result == target
    }

    def "Exists"() {
        given:
        def id = 1L

        when:
        def result = locationService.exists(id)

        then:
        1 * locationRepository.existsById(id) >> true

        result
    }

    def "Search"() {
        given:
        def filter = FilterLocationsDto.builder()
                .city('Warsaw, Poland')
                .descriptionItems(['Has WiFi'])
                .date('2022-01-02')
                .guestCount(10)
                .isSeated(true)
                .build()

        def locationDescription = LocationDescriptionItem.builder()
                .id('Has WiFi')
                .description('Description')
                .build()
        def locationDescriptions = [locationDescription]
        def locations = [fakeFullLocationWithAvailability]

        def target = ImmutableList.of(fakeFullLocationWithAvailability)

        when:
        def result = locationService.search(filter)

        then:
        1 * locationDescriptionItemService.getById(locationDescriptions.iterator().next().getId()) >> _
        1 * locationRepository.searchWithDate(filter.getCity(), filter.getDate()) >> locations

        result == target
    }

    def "FindByCity"() {
        given:
        def city = 'Warsaw'
        def locations = ImmutableList.of(fakeFullLocation)
        def target = locations

        when:
        def result = locationService.findByCity(city)

        then:
        1 * locationRepository.findByLocationAddress_City(city) >> locations

        result == target


    }

    def "Create"() {
    }

    def "Save"() {
        given:
        def location = fakeFullLocation

        when:
        locationService.save(location)

        then:
        1 * locationRepository.save(location)
    }

    def "GetWithAvailability"() {
        given:
        def id = 1L
        def date = '2021-02-01'
        def rating = 5
        def locations = fakeFullLocation
        locations.setRating(rating)

        def target = fakeFullLocation
        when:
        def result = locationService.getWithAvailability(id, date)

        then:
        1 * locationRepository.getByIdWithAvailability(id, date) >> Optional.of(locations)
        1 * locationReviewService.getRating(id) >> rating

        result == target
    }

    def "Edit"() {
    }

    def "GetByBusinessId"() {
        given:
        def id = 1L
        def locations = [fakeFullLocation]

        def target = ImmutableList.of(fakeFullLocation)
        when:
        def result = locationService.getByBusinessId(id)

        then:
        1 * locationRepository.findAllByBusiness_Id(id) >> locations

        result == target
    }

    def "GetWithImages"() {
        given:
        def id = 1L
        def locations = fakeFullLocation

        def target = fakeFullLocation
        when:
        def result = locationService.getWithImages(id)

        then:
        1 * locationRepository.findWithImages(id) >> Optional.of(locations)

        result == target

    }

    def "GetByCateringId"() {
        given:
        def id = 1L
        def location = fakeFullLocation

        def target = ImmutableList.of(location)

        when:
        def result = locationService.getByCateringId(id)

        then:
        1 * locationRepository.findAllByCateringId(id) >> [location]

        result == target
    }

    def "Delete"() {
    }

    def "ModifyAvailabilityAfterBooking"() {
    }
}
