package pjatk.socialeventorganizer.social_event_support.catering.service

import pjatk.socialeventorganizer.social_event_support.address.service.AddressService
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.service.CateringBusinessHoursService
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringItemRepository
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringRepository
import pjatk.socialeventorganizer.social_event_support.cuisine.service.CuisineService
import pjatk.socialeventorganizer.social_event_support.location.model.Location
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService
import pjatk.socialeventorganizer.social_event_support.trait.BusinessHoursTrait
import pjatk.socialeventorganizer.social_event_support.trait.address.AddressTrait
import pjatk.socialeventorganizer.social_event_support.trait.business.BusinessTrait
import pjatk.socialeventorganizer.social_event_support.trait.catering.CateringTrait
import pjatk.socialeventorganizer.social_event_support.trait.location.LocationTrait
import pjatk.socialeventorganizer.social_event_support.trait.user.UserCredentialsTrait
import spock.lang.Specification
import spock.lang.Subject

class CateringServiceTest extends Specification implements UserCredentialsTrait, AddressTrait, CateringTrait, LocationTrait, BusinessTrait, BusinessHoursTrait {

    @Subject
    CateringService cateringService;

    CateringRepository repository;

    CateringItemRepository cateringItemRepository;

    LocationService locationService;

    AddressService addressService;

    SecurityService securityService;

    BusinessRepository businessRepository;

    CateringBusinessHoursService cateringBusinessHoursService;

    CuisineService cuisineService;


    def setup() {
        repository = Mock()
        cateringItemRepository = Mock()
        cateringItemRepository = Mock()
        locationService = Mock()
        addressService = Mock()
        securityService = Mock()
        businessRepository = Mock()
        cateringBusinessHoursService = Mock()
        cuisineService = Mock()

        cateringService = new CateringService(repository, cateringItemRepository, locationService, addressService, securityService, businessRepository, cateringBusinessHoursService, cuisineService)
    }

    def "create  positive test scenario"() {
        given:
        def location = Location.builder().id(1).build()

        def userCredentials = fakeBusinessUserCredentials

        def businessHoursDto = fakeBusinessHoursDto

        def addressDto = fakeAddressDto

        def address = fakeAddress

        def cateringBusinessHours = fakeCateringBusinessHours

        def business = fakeVerifiedBusiness

        def cateringDto = fakeCateringDtoOffersOutsideCatering

        def catering = fakeCateringOffersOutsideCatering

        when:
        cateringService.create(cateringDto, null)

        then:
        1 * addressService.create(addressDto) >> address
        1 * securityService.getUserCredentials() >> userCredentials
        1 * businessRepository.findById(userCredentials.getUserId()) >> Optional.of(business)
        1 * cateringBusinessHoursService.create(businessHoursDto) >> Set.of(cateringBusinessHours)
        2 * repository.saveAndFlush(catering)
        1 * locationService.findByCityWithId(address.getCity())
        1 * locationService.saveLocation(location)

    }

}
