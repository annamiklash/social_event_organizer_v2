package pjatk.socialeventorganizer.social_event_support.catering.service

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.data.domain.PageImpl
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.service.CateringBusinessHoursService
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringItemRepository
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringRepository
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine
import pjatk.socialeventorganizer.social_event_support.cuisine.service.CuisineService
import pjatk.socialeventorganizer.social_event_support.image.repository.CateringImageRepository
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService
import pjatk.socialeventorganizer.social_event_support.trait.BusinessHoursTrait
import pjatk.socialeventorganizer.social_event_support.trait.address.AddressTrait
import pjatk.socialeventorganizer.social_event_support.trait.business.BusinessTrait
import pjatk.socialeventorganizer.social_event_support.trait.catering.CateringTrait
import pjatk.socialeventorganizer.social_event_support.trait.location.LocationTrait
import pjatk.socialeventorganizer.social_event_support.trait.page.PageTrait
import pjatk.socialeventorganizer.social_event_support.trait.user.UserCredentialsTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class CateringServiceTest extends Specification
        implements UserCredentialsTrait,
                AddressTrait,
                CateringTrait,
                LocationTrait,
                BusinessTrait,
                BusinessHoursTrait,
                PageTrait {

    @Subject
    CateringService cateringService

    CateringRepository cateringRepository
    CateringItemRepository cateringItemRepository
    LocationService locationService
    AddressService addressService
    SecurityService securityService
    BusinessRepository businessRepository
    CateringBusinessHoursService cateringBusinessHoursService
    CuisineService cuisineService
    CateringImageRepository cateringImageRepository
    TimestampHelper timestampHelper

    def setup() {
        cateringRepository = Mock()
        cateringItemRepository = Mock()
        cateringItemRepository = Mock()
        locationService = Mock()
        addressService = Mock()
        securityService = Mock()
        businessRepository = Mock()
        cateringBusinessHoursService = Mock()
        cuisineService = Mock()
        timestampHelper = Mock()
        cateringImageRepository = Mock()

        cateringService = new CateringService(cateringRepository,
                cateringItemRepository,
                locationService,
                addressService,
                securityService,
                businessRepository,
                cateringBusinessHoursService,
                cuisineService,
                timestampHelper,
                cateringImageRepository)
    }

    def "list() positive test scenario"() {
        given:
        def customPage = fakePage
        def keyword = 'no'

        def paging = fakePaging
        def page = new PageImpl<>([fakeCateringOffersOutsideCatering])

        def target = ImmutableList.of(fakeCateringOffersOutsideCatering)

        when:
        def result = cateringService.list(customPage, keyword)

        then:
        1 * cateringRepository.findAllWithKeyword(paging, keyword) >> page

        result == target
    }

    def "get() positive test scenario"() {
        given:
        def id = 1L
        def catering = fakeCateringOffersOutsideCatering

        def target = catering
        when:
        def result = cateringService.get(id)

        then:
        1 * cateringRepository.findById(id) >> Optional.of(catering)

        result == target
    }

    def "getWithDetail() positive test scenario"() {
        given:
        def id = 1L
        def catering = fakeCateringOffersOutsideCatering

        def target = catering
        when:
        def result = cateringService.getWithDetail(id)

        then:
        1 * cateringRepository.findByIdWithDetail(id) >> Optional.of(catering)

        result == target
    }

    def "getByBusinessId() positive test scenario"() {
        given:
        def id = 1L
        def caterings = [fakeCateringOffersOutsideCatering]

        def target = ImmutableList.of(fakeCateringOffersOutsideCatering)
        when:
        def result = cateringService.getByBusinessId(id)

        then:
        1 * cateringRepository.findAllByBusiness_Id(id) >> caterings

        result == target
    }

    def "create  positive test scenario"() {
        given:
        def userCredentials = fakeBusinessUserCredentials
        def address = fakeAddress
        def cateringBusinessHours = Set.of(fakeCateringBusinessHours)
        def business = fakeVerifiedBusiness
        def cateringDto = fakeCateringDtoOffersOutsideCatering
        def businessHoursDto = [fakeBusinessHoursDto]
        cateringDto.setBusinessHours(businessHoursDto)

        def cuisineDto = cateringDto.getCuisines().get(0)
        def catering = fakeCatering
        def now = LocalDateTime.parse('2007-12-03T10:15:30')
        def cuisine = Cuisine.builder().name(cuisineDto.getName()).build()

        def cateringSet = new HashSet<Catering>()
        cateringSet.add(catering)
        def location = fakeLocation
        location.setCaterings(cateringSet)
        def locations = ImmutableList.of(location)

        catering.setCateringAddress(address)
        catering.setBusiness(business)
        catering.setCateringBusinessHours(ImmutableSet.copyOf(cateringBusinessHours))
        catering.setCreatedAt(now)
        catering.setModifiedAt(now)
        catering.setLocations(new HashSet<>())
        catering.setServiceCost(new BigDecimal("100.20"))
        catering.setRating(0.0)

        def target = Catering.builder().build()
        InvokerHelper.setProperties(target, catering.properties)
        target.setCuisines(Set.of(cuisine))

        when:
        def result = cateringService.create(cateringDto, null)

        then:
        1 * securityService.getUserCredentials() >> userCredentials
        1 * businessRepository.findById(userCredentials.getUserId()) >> Optional.of(business)

        1 * addressService.create(cateringDto.getAddress()) >> address
        1 * cateringBusinessHoursService.create(businessHoursDto) >> cateringBusinessHours
        2 * timestampHelper.now() >> now
        1 * cuisineService.getByName(cuisineDto.getName()) >> cuisine

        1 * locationService.findByCityWithId(address.getCity()) >> locations

        1 * cateringRepository.saveAndFlush(_)

        result == target
    }

    def "getWithBusinessHours() positive test scenario"() {
        given:
        def cateringId = 1L
        def catering = fakeCatering

        def target = catering
        when:
        def result = cateringService.getWithBusinessHours(cateringId)

        then:
        1 * cateringRepository.getWithBusinessHours(cateringId) >> Optional.of(catering)

        result == target
    }

    def "edit() positive test scenario"() {
        given:
        def cateringId = 1L
        def dto = fakeCateringDtoOffersOutsideCatering
        def now = LocalDateTime.parse('2007-12-03T10:15:30')

        def catering = fakeCatering
        catering.setEmail(dto.getEmail())
        catering.setName(dto.getName())
        catering.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()))
        catering.setServiceCost(Converter.convertPriceString(dto.getServiceCost()))
        catering.setDescription(dto.getDescription())
        catering.setModifiedAt(now)

        def target = catering

        when:
        def result = cateringService.edit(cateringId, dto)

        then:
        1 * cateringRepository.findById(cateringId) >> Optional.of(catering)
        1 * timestampHelper.now() >> now
        1 * cateringRepository.save(catering)

        result == target
    }

    def "deleteLogical() positive test scenario"() {
        given:
        def id = 1L
        def catering = fakeCatering
        def now = LocalDateTime.parse('2007-12-03T10:15:30')

        when:
        cateringService.deleteLogical(id)

        then:
        1 * cateringRepository.findAllCateringInformation(id) >> Optional.of(catering)
        1 * addressService.delete(1l)
        2 * timestampHelper.now() >> now

        noExceptionThrown()
    }

    def "cateringWithIdExists() positive test scenario"() {
        given:
        def id = 1L

        when:
        def result = cateringService.cateringWithIdExists(id)

        then:
        1 * cateringRepository.existsById(id) >> true

        result
    }

    def "getWithImages() positive test scenario"() {
        given:
        def id = 1L
        def catering = fakeCatering

        def target = catering

        when:
        def result = cateringService.getWithImages(id)

        then:
        1 * cateringRepository.findWithImages(id) >> Optional.of(catering)

        result == target
    }

    def "getByLocationId() positive test scenario"() {
        given:
        def id = 1L
        def catering = fakeCatering

        def target = ImmutableList.of(catering)

        when:
        def result = cateringService.getByLocationId(id)

        then:
        1 * cateringRepository.findAllByLocationId(id) >> [catering]

        result == target
    }

    def "count() positive test scenario"() {
        given:
        def keyword = '123'

        def target = 123L

        when:
        def result = cateringService.count(keyword)

        then:
        1 * cateringRepository.countAll(keyword) >> target

        result == target
    }

}
