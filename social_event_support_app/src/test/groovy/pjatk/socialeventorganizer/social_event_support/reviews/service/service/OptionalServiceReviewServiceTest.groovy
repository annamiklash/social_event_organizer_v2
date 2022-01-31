package pjatk.socialeventorganizer.social_event_support.reviews.service.service

import com.google.common.collect.ImmutableList
import org.springframework.data.domain.PageImpl
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository
import pjatk.socialeventorganizer.social_event_support.optional_service.repository.OptionalServiceRepository
import pjatk.socialeventorganizer.social_event_support.reviews.service.repository.OptionalServiceReviewRepository
import pjatk.socialeventorganizer.social_event_support.trait.customer.CustomerTrait
import pjatk.socialeventorganizer.social_event_support.trait.optional_service.OptionalServiceTrait
import pjatk.socialeventorganizer.social_event_support.trait.page.PageTrait
import pjatk.socialeventorganizer.social_event_support.trait.reviews.ReviewTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class OptionalServiceReviewServiceTest extends Specification implements OptionalServiceTrait,
        CustomerTrait, ReviewTrait, PageTrait {

    @Subject
    OptionalServiceReviewService optionalServiceReviewService

    OptionalServiceReviewRepository optionalServiceReviewRepository
    CustomerRepository customerRepository
    OptionalServiceRepository optionalServiceRepository
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {
        optionalServiceReviewRepository = Mock()
        customerRepository = Mock()
        optionalServiceRepository = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        optionalServiceReviewService = new OptionalServiceReviewService(optionalServiceReviewRepository,
                customerRepository,
                optionalServiceRepository,
                timestampHelper)
    }
    def "LeaveServiceReview"() {

        given:
        def customerId = 1l
        def locationId = 1l
        def reviewDto = fakeReviewDtoNoId
        def customer = fakeCustomer
        def service = fakeOptionalService

        def serviceReview = fakeServiceReviewNoId
        serviceReview.setOptionalService(service);
        serviceReview.setCustomer(customer);
        serviceReview.setCreatedAt(now);

        def target = serviceReview

        when:
        def result = optionalServiceReviewService.leaveServiceReview(customerId, locationId, reviewDto)

        then:
        1 * customerRepository.findById(customerId) >> Optional.of(customer)
        1 * optionalServiceRepository.findById(locationId) >> Optional.of(service)
        1 * optionalServiceReviewRepository.save(serviceReview)

        result == target
    }

    def "GetByServiceId with Paging"() {
        given:
        def serviceId = 1l
        def customPage = fakePage
        def paging = fakePaging
        def page = new PageImpl<>([fakeServiceReview])

        def target = ImmutableList.of(fakeServiceReview)

        when:
        def result = optionalServiceReviewService.getByServiceId(customPage, serviceId)

        then:
        1 * optionalServiceReviewRepository.existsByOptionalService_Id(serviceId) >> true
        1 * optionalServiceReviewRepository.getByServiceId(serviceId, paging) >> page

        result == target

    }

    def "GetRating"() {
        given:
        def serviceId = 1l
        def reviews = [fakeServiceReview]
        def target = fakeServiceReview.getStarRating()

        when:
        def result = optionalServiceReviewService.getRating(serviceId)

        then:
        1 * optionalServiceReviewRepository.getByServiceId(serviceId) >> reviews

        result == target
    }

    def "Delete"() {
        given:
        def serviceReview = fakeServiceReview

        when:
        optionalServiceReviewService.delete(serviceReview)

        then:
        1 * optionalServiceReviewRepository.delete(serviceReview)
    }

    def "Count"() {
        given:
        def serviceId = 1l
        def target = 1l

        when:
        def result = optionalServiceReviewService.count(serviceId)

        then:
        1 *  optionalServiceReviewRepository.countAllByOptionalService_Id(serviceId) >> 1l

        result == target
    }
}
