package pjatk.socialeventorganizer.social_event_support.reviews.location.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage
import pjatk.socialeventorganizer.social_event_support.reviews.location.service.LocationReviewService
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper
import pjatk.socialeventorganizer.social_event_support.table.TableDto
import pjatk.socialeventorganizer.social_event_support.test_helper.TestSerializer
import pjatk.socialeventorganizer.social_event_support.trait.reviews.ReviewDtoTrait
import pjatk.socialeventorganizer.social_event_support.trait.reviews.location.LocationReviewTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [LocationReviewController.class])
class LocationReviewControllerTest extends Specification
        implements LocationReviewTrait,
                ReviewDtoTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private LocationReviewService locationReviewService

    @WithMockUser
    def "GET api/reviews/location/allowed/all returns 200 positive test scenario"() {
        given:
        def locationId = 1L
        def pageNo = 1
        def pageSize = 50
        def sortBy = 'id'
        def order = 'desc'

        def count = 1L

        def customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build()

        def locationReview = fakeLocationReview
        def reviewList = ImmutableList.of(locationReview)
        def reviewDto = ReviewMapper.toDto(locationReview)
        def resultList = ImmutableList.of(reviewDto)
        def locationTableDto =
                new TableDto<>(new TableDto.MetaDto(count, pageNo, pageSize, sortBy), resultList)
        def jsonResponse = TestSerializer.serialize(locationTableDto)

        BDDMockito.given(locationReviewService.getByLocationId(eq(customPage), eq(locationId)))
                .willReturn(reviewList)
        BDDMockito.given(locationReviewService.count(eq(locationId)))
                .willReturn(count)

        expect:
        mockMvc.perform(
                get('/api/reviews/location/allowed/all')
                        .param('locationId', locationId.toString())
                        .param('pageNo', pageNo.toString())
                        .param('pageSize', pageSize.toString())
                        .param('sortBy', sortBy)
                        .param('order', order)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "POST api/reviews/location returns 200 positive test scenario"() {
        given:
        def customerId = 1L
        def locationId = 2L
        def dto = fakeReviewDto

        def locationReview = fakeLocationReview
        def result = ReviewMapper.toDto(locationReview)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(locationReviewService.leaveLocationReview(eq(customerId), eq(locationId), eq(dto)))
                .willReturn(locationReview)

        expect:
        mockMvc.perform(
                post("/api/reviews/location")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('customerId', customerId.toString())
                        .param('locationId', locationId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }
}
