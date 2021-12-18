package pjatk.socialeventorganizer.social_event_support.catering.service


import pjatk.socialeventorganizer.social_event_support.address.service.AddressService
import pjatk.socialeventorganizer.social_event_support.business.model.dto.BusinessDto
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository
import pjatk.socialeventorganizer.social_event_support.businesshours.DayEnum
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.model.CateringBusinessHours
import pjatk.socialeventorganizer.social_event_support.businesshours.catering.service.CateringBusinessHoursService
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringDto
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringItemRepository
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringRepository
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine
import pjatk.socialeventorganizer.social_event_support.cuisine.service.CuisineService
import pjatk.socialeventorganizer.social_event_support.location.model.Location
import pjatk.socialeventorganizer.social_event_support.location.service.LocationService
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService
import pjatk.socialeventorganizer.social_event_support.trait.UserCredentialsTrait
import pjatk.socialeventorganizer.social_event_support.trait.address.AddressTrait
import pjatk.socialeventorganizer.social_event_support.trait.business.BusinessTrait
import pjatk.socialeventorganizer.social_event_support.trait.catering.CateringTrait
import pjatk.socialeventorganizer.social_event_support.trait.location.LocationTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalTime

class CateringServiceTest extends Specification implements UserCredentialsTrait, AddressTrait, CateringTrait, LocationTrait, BusinessTrait {

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

        def businessDto = BusinessDto.builder()
                .id(1)
                .businessName('Name')
                .address(addressDto)
                .verificationStatus('VERIFIED')
                .build();

        def business = fakeVerifiedBusiness

        def cateringDto = CateringDto.builder()
                .name('Name')
                .phoneNumber('123456789')
                .serviceCost('100.20')
                .offersOutsideCatering(true)
                .address(addressDto)
                .business(businessDto)
                .build()

        def cuisines = Set.of(Cuisine.builder().id(1).name('Cuisine').build())

        def businessHours = CateringBusinessHours.builder()
                .id(1)
                .day(DayEnum.MONDAY.name())
                .timeFrom(LocalTime.of(10, 0))
                .timeTo(LocalTime.of(20, 0))
                .build()

        def businessHOursSet = new HashSet(List.of(business))

        def catering = Catering.builder()
                .name('Name')
                .business(business)
                .cuisines(cuisines)
                .build()


        when:
        cateringService.create(cateringDto, null)

        then:
        1 * addressService.create(addressDto) >> address
        1 * securityService.getUserCredentials() >> userCredentials
        1 * businessRepository.findById(userCredentials.getUserId()) >> Optional.of(business)
        1 * cateringBusinessHoursService.create(businessHoursDto) >> List.of(businessHours)
        2 * repository.saveAndFlush(catering)
        1 * locationService.findByCityWithId(address.getCity())
        1 * locationService.saveLocation(location)

    }

}
