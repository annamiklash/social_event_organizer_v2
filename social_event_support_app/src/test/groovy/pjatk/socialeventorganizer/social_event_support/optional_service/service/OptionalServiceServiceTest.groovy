package pjatk.socialeventorganizer.social_event_support.optional_service.service

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import org.springframework.data.domain.PageImpl
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository.OptionalServiceAvailabilityRepository
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository
import pjatk.socialeventorganizer.social_event_support.businesshours.service.service.OptionalServiceBusinessHoursService
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper
import pjatk.socialeventorganizer.social_event_support.exceptions.BusinessVerificationException
import pjatk.socialeventorganizer.social_event_support.image.repository.OptionalServiceImageRepository
import pjatk.socialeventorganizer.social_event_support.optional_service.model.interpreter.translation.service.TranslationLanguageService
import pjatk.socialeventorganizer.social_event_support.optional_service.model.music.musicstyle.service.MusicStyleService
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.repostory.OptionalServiceForChosenLocationRepository
import pjatk.socialeventorganizer.social_event_support.optional_service.repository.OptionalServiceRepository
import pjatk.socialeventorganizer.social_event_support.reviews.service.service.OptionalServiceReviewService
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService
import pjatk.socialeventorganizer.social_event_support.trait.BusinessHoursTrait
import pjatk.socialeventorganizer.social_event_support.trait.address.AddressTrait
import pjatk.socialeventorganizer.social_event_support.trait.availability.AvailabilityTrait
import pjatk.socialeventorganizer.social_event_support.trait.business.BusinessTrait
import pjatk.socialeventorganizer.social_event_support.trait.optional_service.OptionalServiceTrait
import pjatk.socialeventorganizer.social_event_support.trait.page.PageTrait
import pjatk.socialeventorganizer.social_event_support.trait.user.UserCredentialsTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class OptionalServiceServiceTest extends Specification
        implements OptionalServiceTrait,
                AddressTrait,
                BusinessHoursTrait,
                UserCredentialsTrait,
                PageTrait,
                BusinessTrait,
                AvailabilityTrait {

    @Subject
    OptionalServiceService optionalServiceService

    OptionalServiceRepository optionalServiceRepository
    BusinessRepository businessRepository
    SecurityService securityService
    AddressService addressService
    MusicStyleService musicStyleService
    OptionalServiceBusinessHoursService optionalServiceBusinessService
    TranslationLanguageService translationLanguageService
    OptionalServiceAvailabilityRepository optionalServiceAvailabilityRepository
    OptionalServiceForChosenLocationRepository optionalServiceForChosenLocationRepository
    OptionalServiceImageRepository optionalServiceImageRepository
    OptionalServiceReviewService optionalServiceReviewService
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {

        optionalServiceRepository = Mock()
        businessRepository = Mock()
        securityService = Mock()
        addressService = Mock()
        musicStyleService = Mock()
        optionalServiceBusinessService = Mock()
        translationLanguageService = Mock()
        optionalServiceAvailabilityRepository = Mock()
        optionalServiceForChosenLocationRepository = Mock()
        optionalServiceImageRepository = Mock()
        optionalServiceReviewService = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        optionalServiceService = new OptionalServiceService(
                optionalServiceRepository,
                businessRepository,
                securityService,
                addressService,
                musicStyleService,
                optionalServiceBusinessService,
                translationLanguageService,
                optionalServiceAvailabilityRepository,
                optionalServiceForChosenLocationRepository,
                optionalServiceReviewService,
                optionalServiceImageRepository,
                timestampHelper)
    }

    def "List"() {
        given:
        def customPage = fakePage
        def keyword = 'no'

        def paging = fakePaging
        def page = new PageImpl<>([fakeOptionalService])

        def target = ImmutableList.of(fakeOptionalService)

        when:
        def result = optionalServiceService.list(customPage, keyword)

        then:
        1 * optionalServiceRepository.findAllWithKeyword(paging, keyword) >> page

        result == target
    }

    def "Get"() {
        given:
        def id = 1L
        def optionalService = fakeOptionalService

        def target = optionalService
        when:
        def result = optionalServiceService.get(id)

        then:
        1 * optionalServiceRepository.findWithDetail(id) >> Optional.of(optionalService)

        result == target
    }

    def "GetCities"() {
        given:
        def target = List.of("Warsaw, Poland")

        when:
        def result = optionalServiceService.getCities()

        then:
        1 * optionalServiceRepository.findDistinctCities() >> List.of("Warsaw, Poland")

        result == target
    }

    def "GetWithDetail"() {
        given:
        def id = 1L
        def optionalService = fakeOptionalService

        def target = optionalService
        when:
        def result = optionalServiceService.getWithDetail(id)

        then:
        1 * optionalServiceRepository.findWithDetail(id) >> Optional.of(optionalService)

        result == target
    }

    def "Create BusinessVerificationException"() {
        given:
        def userCredentials = fakeBusinessUserCredentials
        def business = fakeVerifiedBusiness
        business.setVerificationStatus('NOT_VERIFIED')
        def serviceDto = fakeOptionalServiceHostDto
        def businessHoursDto = [fakeBusinessHoursDto]
        serviceDto.setBusinessHours(businessHoursDto)

        when:
        optionalServiceService.create(serviceDto)

        then:
        1 * securityService.getUserCredentials() >> userCredentials
        1 * businessRepository.findById(userCredentials.getUserId()) >> Optional.of(business)
        thrown(BusinessVerificationException.class)
    }

    def "Create HOST"() {
        given:
        def userCredentials = fakeBusinessUserCredentials
        def address = fakeAddress
        def serviceBusinessHours = List.of(fakeServiceBusinessHours)
        def business = fakeVerifiedBusiness
        def serviceDto = fakeOptionalServiceHostDto
        def host = fakeOptionalHost
        def businessHoursDto = [fakeBusinessHoursDto]
        def availability = fakeServiceAvailability
        availability.setOptionalService(host)
        serviceDto.setBusinessHours(businessHoursDto)

        host.setId(null)
        host.setServiceAddress(address)
        host.setBusiness(business)
        host.setOptionalServiceBusinessHours(ImmutableSet.copyOf(serviceBusinessHours))
        host.setCreatedAt(now)
        host.setModifiedAt(now)
        host.setServiceCost(new BigDecimal('123.00'))
        host.setRating(0.0)

        def target = host

        when:
        def result = optionalServiceService.create(serviceDto)

        then:
        1 * securityService.getUserCredentials() >> userCredentials
        1 * businessRepository.findById(userCredentials.getUserId()) >> Optional.of(business)
        1 * addressService.create(serviceDto.getAddress()) >> address
        1 * optionalServiceBusinessService.create(businessHoursDto) >> serviceBusinessHours
        1 * optionalServiceRepository.save(_)
//        1 * optionalServiceAvailabilityRepository.save(availability)

        result == target
    }

    def "Edit"() {

    }

    def "Search"() {

    }

    def "IsAvailable"() {
        given:
        def id = 1L
        def date = '2022-01-01'
        def timeFrom = '11:00'
        def timeTo = '17:00'
        def dateTimeFrom = date + " " + timeFrom
        def dateTimeTo = date + " " + timeTo

        def target = true

        when:
        def result = optionalServiceService.isAvailable(id, date, timeFrom, timeTo)

        then:
        1 * optionalServiceRepository.available(id, date, dateTimeFrom, dateTimeTo) >> Optional.of(fakeOptionalService)

        result == target
    }

    def "GetWithImages"() {
        given:
        def id = 1L
        def service = fakeOptionalService

        def target = fakeOptionalService

        when:
        def result = optionalServiceService.getWithImages(id)

        then:
        1 * optionalServiceRepository.findWithImages(id) >> Optional.of(service)

        result == target

    }

    def "Count"() {
        given:
        def keyword = 'no'

        def target = 1L

        when:
        def result = optionalServiceService.count(keyword)

        then:
        1 * optionalServiceRepository.countAll(keyword) >> target

        result == target
    }

    def "GetByBusinessId"() {
        given:
        def id = 1L
        def services = [fakeOptionalService]

        def target = ImmutableList.of(fakeOptionalService)

        when:
        def result = optionalServiceService.getByBusinessId(id)

        then:
        1 * optionalServiceRepository.findAllByBusiness_Id(id) >> services

        result == target
    }

    def "Delete"() {

    }
}
