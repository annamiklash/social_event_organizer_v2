package pjatk.socialeventorganizer.social_event_support.business.service

import com.google.common.collect.ImmutableList
import org.springframework.data.domain.PageImpl
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService
import pjatk.socialeventorganizer.social_event_support.common.util.TimestampHelper
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService
import pjatk.socialeventorganizer.social_event_support.trait.address.AddressTrait
import pjatk.socialeventorganizer.social_event_support.trait.business.BusinessTrait
import pjatk.socialeventorganizer.social_event_support.trait.page.PageTrait
import pjatk.socialeventorganizer.social_event_support.trait.user.UserTrait
import pjatk.socialeventorganizer.social_event_support.user.service.UserService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

import static pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum.NOT_VERIFIED

class BusinessServiceTest extends Specification
        implements PageTrait, BusinessTrait, AddressTrait, UserTrait {

    @Subject
    BusinessService businessService

    BusinessRepository businessRepository
    AddressService addressService
    UserService userService
    LocationService locationService
    CateringService cateringService
    OptionalServiceService optionalServiceService
    TimestampHelper timestampHelper

    def setup() {
        businessRepository = Mock()
        addressService = Mock()
        userService = Mock()
        locationService = Mock()
        cateringService = Mock()
        optionalServiceService = Mock()
        timestampHelper = Mock()

        businessService = new BusinessService(businessRepository,
                addressService,
                userService,
                locationService,
                cateringService,
                optionalServiceService,
                timestampHelper
        )
    }

    def "list() positive test scenario"() {
        given:
        def customPage = fakePage
        def paging = fakePaging
        def page = new PageImpl<>([fakeVerifiedBusiness])
        def target = ImmutableList.of(fakeVerifiedBusiness)

        when:
        def result = businessService.list(customPage)

        then:
        1 * businessRepository.findAll(paging) >> page

        result == target
    }

    def "createBusinessAccount() positive test scenario"() {
        given:
        def businessDto = fakeVerifiedBusinessDto
        def address = fakeAddress

        def now = LocalDateTime.parse('2007-12-03T10:15:30')
        def user = fakeUser
        user.active = true
        user.modifiedAt = now
        user.type = 'B'

        def target = fakeVerifiedBusiness
        target.id = user.id
        target.verificationStatus = NOT_VERIFIED.name()
        target.user = user
        target.address = address
        target.services = null
        target.caterings = null
        target.locations = null

        when:
        def result = businessService.createBusinessAccount(businessDto)

        then:
        1 * addressService.create(businessDto.getAddress()) >> address
        1 * userService.get(businessDto.getUser().getId()) >> user
        1 * timestampHelper.now() >> now
        1 * userService.save(user)
        1 * businessRepository.save(target)

        result == target
    }

    def "edit() positive test scenario"() {
        given:
        def businessId = 1L
        def businessDto = fakeVerifiedBusinessDto

        def business = fakeVerifiedBusiness

        def target = fakeVerifiedBusiness

        when:
        def result = businessService.edit(businessId, businessDto)

        then:
        1 * businessRepository.getWithAddress(businessId) >> Optional.of(business)
        1 * addressService.edit(business.getAddress().getId(), businessDto.getAddress())
        1 * businessRepository.save(business)

        result == target
    }

    def "getWithDetail() positive test scenario"() {
        given:
        def businessId = 1L

        def business = fakeVerifiedBusiness

        def target = fakeVerifiedBusiness

        when:
        def result = businessService.getWithDetail(businessId)

        then:
        1 * businessRepository.getWithDetail(businessId) >> Optional.of(business)

        result == target
    }

    def "get() positive test scenario"() {
        given:
        def businessId = 1L

        def business = fakeVerifiedBusiness

        def target = fakeVerifiedBusiness

        when:
        def result = businessService.get(businessId)

        then:
        1 * businessRepository.findById(businessId) >> Optional.of(business)

        result == target
    }

    def "deleteLogical() positive test scenario"() {
        given:
        def businessId = 1L

        def business = fakeVerifiedBusiness

        when:
        businessService.deleteLogical(businessId)

        then:
        1 * businessRepository.findAllBusinessInformation(businessId) >> Optional.of(business)
        1 * locationService.deleteLogical(business.getLocations().iterator().next().getId())
        1 * cateringService.deleteLogical(business.getCaterings().iterator().next().getId())
        1 * optionalServiceService.deleteLogical(business.getServices().iterator().next().getId())
        1 * addressService.delete(business.getAddress())
        1 * userService.delete(business.getUser())
        1 * businessRepository.save(business)
    }

    def "verify() positive test scenario"() {
        given:
        def businessId = 1L

        def business = fakeVerifiedBusiness

        def target = fakeVerifiedBusiness
        target.verificationStatus = 'VERIFIED'

        when:
        def result = businessService.verify(businessId)

        then:
        1 * businessRepository.findById(businessId) >> Optional.of(business)
        1 * businessRepository.save(business)

        result == target
    }
}
