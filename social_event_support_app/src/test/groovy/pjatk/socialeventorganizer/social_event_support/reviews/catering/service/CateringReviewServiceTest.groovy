package pjatk.socialeventorganizer.social_event_support.reviews.catering.service

import com.google.common.collect.ImmutableList
import org.springframework.data.domain.PageImpl
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringService
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService
import pjatk.socialeventorganizer.social_event_support.reviews.catering.repository.CateringReviewRepository
import pjatk.socialeventorganizer.social_event_support.trait.catering.CateringTrait
import pjatk.socialeventorganizer.social_event_support.trait.customer.CustomerTrait
import pjatk.socialeventorganizer.social_event_support.trait.page.PageTrait
import pjatk.socialeventorganizer.social_event_support.trait.reviews.ReviewTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class CateringReviewServiceTest extends Specification implements CateringTrait, CustomerTrait, ReviewTrait, PageTrait {

    @Subject
    CateringReviewService cateringReviewService

    CateringReviewRepository cateringReviewRepository
    CustomerService customerService
    CateringService cateringService
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {
        cateringReviewRepository = Mock()
        customerService = Mock()
        cateringService = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        cateringReviewService = new CateringReviewService(cateringReviewRepository,
                customerService,
                cateringService,
                timestampHelper)
    }

    def "LeaveCateringReview"() {
//        given:
//        def customerId = 1l
//        def cateringId = 1l
//        def reviewDto = fakeReviewDtoNoId
//        def customer = fakeCustomer
//        def catering = fakeCateringWithDetails
//        def cateringReview = fakeCateringReview
//
//        def target = cateringReview
//
//        target.setCatering(catering);
//        target.setCustomer(customer);
//        target.setCreatedAt(now)
//
//        when:
//        def result = cateringReviewService.leaveCateringReview(customerId, cateringId, reviewDto)
//
//        then:
//        1 * customerService.get(customerId) >> customer
//        1 * cateringService.get(cateringId) >> catering
//        1 *  cateringReviewRepository.save(cateringReview)
//
//        result == target
    }

    def "GetByCateringId with Paging"() {
        given:
        def cateringId = 1l
        def customPage = fakePage
        def paging = fakePaging
        def page = new PageImpl<>([fakeCateringReview])

        def target = ImmutableList.of(fakeCateringReview)

        when:
        def result = cateringReviewService.getByCateringId(customPage, cateringId)

        then:
        1 * cateringReviewRepository.existsCateringReviewByCatering_Id(cateringId) >> true
        1 * cateringReviewRepository.getByCateringId(cateringId, paging) >> page

        result == target
    }

    def "GetByCateringId"() {
        given:
        def cateringId = 1l
        def reviews = [fakeCateringReview]
        def target = reviews

        when:
        def result = cateringReviewService.getByCateringId(cateringId)

        then:
        1 * cateringReviewRepository.existsCateringReviewByCatering_Id(cateringId) >> true
        1 * cateringReviewRepository.getByCateringId(cateringId) >> reviews

        result == target
    }

    def "GetRating"() {
        given:
        def cateringId = 1l
        def reviews = [fakeCateringReview]
        def target = fakeLocationReview.getStarRating()

        when:
        def result = cateringReviewService.getRating(cateringId)

        then:
        1 * cateringReviewRepository.getByCateringId(cateringId) >> reviews

        result == target
    }

    def "Count"() {
        given:
        def cateringId = 1l
        def target = 1l

        when:
        def result = cateringReviewService.count(cateringId)

        then:
        1 * cateringReviewRepository.countAllByCatering_Id(cateringId) >> 1l

        result == target
    }
}
