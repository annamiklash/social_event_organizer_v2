package pjatk.socialeventorganizer.social_event_support.business.service

import com.google.common.collect.ImmutableList
import org.springframework.data.domain.PageImpl
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService
import pjatk.socialeventorganizer.social_event_support.business.mapper.BusinessMapper
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService
import pjatk.socialeventorganizer.social_event_support.security.password.PasswordEncoderSecurity
import pjatk.socialeventorganizer.social_event_support.trait.address.AddressTrait
import pjatk.socialeventorganizer.social_event_support.trait.business.BusinessTrait
import pjatk.socialeventorganizer.social_event_support.trait.catering.CateringTrait
import pjatk.socialeventorganizer.social_event_support.trait.location.LocationTrait
import pjatk.socialeventorganizer.social_event_support.trait.optional_service.OptionalServiceTrait
import pjatk.socialeventorganizer.social_event_support.trait.page.PageTrait
import pjatk.socialeventorganizer.social_event_support.trait.user.BusinessUserRegistrationDtoTrait
import pjatk.socialeventorganizer.social_event_support.trait.user.UserTrait
import pjatk.socialeventorganizer.social_event_support.user.service.UserService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class BusinessServiceTest extends Specification
        implements PageTrait,
                BusinessTrait,
                AddressTrait,
                UserTrait,
                BusinessUserRegistrationDtoTrait,
                LocationTrait,
                CateringTrait,
                OptionalServiceTrait {

    @Subject
    BusinessService businessService

    BusinessRepository businessRepository
    AddressService addressService
    UserService userService
    LocationService locationService
    CateringService cateringService
    OptionalServiceService optionalServiceService
    TimestampHelper timestampHelper
    PasswordEncoderSecurity passwordEncoderSecurity

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {
        businessRepository = Mock()
        addressService = Mock()
        userService = Mock()
        locationService = Mock()
        cateringService = Mock()
        optionalServiceService = Mock()
        timestampHelper = Mock()
        passwordEncoderSecurity = Mock()

        timestampHelper.now() >> now

        businessService = new BusinessService(businessRepository,
                addressService,
                userService,
                locationService,
                cateringService,
                optionalServiceService,
                passwordEncoderSecurity,
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
        def businessDto = fakeBusinessUserRegistrationDto
        def address = fakeAddress
        def hashedPassword = 'hashedPassword'

        def user = fakeUser
        user.active = true
        user.modifiedAt = now
        user.type = 'B'

        def target = BusinessMapper.fromBusinessUserRegistrationDto(businessDto)
        target.password = hashedPassword
        target.address = address
        target.createdAt = now
        target.modifiedAt = now

        when:
        def result = businessService.createBusinessAccount(businessDto)

        then:
        1 * userService.userExists(businessDto.getEmail()) >> false
        1 * addressService.create(businessDto.getAddress()) >> address
        1 * passwordEncoderSecurity.bcryptEncryptor(businessDto.getPassword()) >> hashedPassword
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

    def "delete() positive test scenario"() {
        given:
        def businessId = 1L
        def locationsSet = Set.of(fakeLocation)
        def cateringsSet = Set.of(fakeCatering)
        def serviceSet = Set.of(fakeOptionalService)

        def business = fakeVerifiedBusiness

        when:
        businessService.delete(businessId)

        then:
        1 * businessRepository.findAllBusinessInformation(businessId) >> Optional.of(business)

        1 * locationService.getByBusinessId(businessId) >> ImmutableList.copyOf(locationsSet)
        1 * cateringService.getByBusinessId(businessId) >> ImmutableList.copyOf(cateringsSet)
        1 * optionalServiceService.getByBusinessId(businessId) >> ImmutableList.copyOf(serviceSet)

        1 * locationService.delete(locationsSet.iterator().next().getId())
        1 * cateringService.delete(cateringsSet.iterator().next().getId())
        1 * optionalServiceService.delete(serviceSet.iterator().next().getId())
        1 * addressService.delete(business.getAddress())
        1 * businessRepository.delete(business)
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
