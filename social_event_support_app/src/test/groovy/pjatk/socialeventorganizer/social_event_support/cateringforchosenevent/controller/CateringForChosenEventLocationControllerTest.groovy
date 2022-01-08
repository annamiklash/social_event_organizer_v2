package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper.CateringForChosenLocationMapper
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service.CateringForChosenEventLocationService
import pjatk.socialeventorganizer.social_event_support.event.mapper.OrganizedEventMapper
import pjatk.socialeventorganizer.social_event_support.test_helper.TestSerializer
import pjatk.socialeventorganizer.social_event_support.trait.cateringforchosenevent.CateringForChosenEventLocationTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [CateringForChosenEventLocationController.class])
class CateringForChosenEventLocationControllerTest extends Specification
        implements CateringForChosenEventLocationTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private CateringForChosenEventLocationService cateringForChosenEventLocationService

    @WithMockUser(authorities = ['BUSINESS'])
    def "PUT api/event/catering/confirm returns 200 positive test scenario"() {
        given:
        def cateringId = 1L
        def eventId = 2L

        def catering = fakeFullCateringForChosenEventLocation
        def result = CateringForChosenLocationMapper.toDto(catering)

        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(cateringForChosenEventLocationService.confirmReservation(eq(cateringId), eq(eventId)))
                .willReturn(catering)

        expect:
        mockMvc.perform(
                put("/api/event/catering/confirm")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('cateringId', cateringId.toString())
                        .param('eventId', eventId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "GET api/event/catering/status returns 200 positive test scenario"() {
        given:
        def statusStr = 'SAMPLE STATUS'
        def cateringId = 1L

        def cateringForChosenEventLocation = fakeFullCateringForChosenEventLocation
        def cateringForChosenEventLocationList = ImmutableList.of(cateringForChosenEventLocation)
        def cateringForChosenEventLocationDto = CateringForChosenLocationMapper.toDto(cateringForChosenEventLocation)
        def resultList = ImmutableList.of(cateringForChosenEventLocationDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(cateringForChosenEventLocationService.listAllByStatus(eq(cateringId), eq(statusStr)))
                .willReturn(cateringForChosenEventLocationList)

        expect:
        mockMvc.perform(
                get("/api/event/catering/status")
                        .param("status", statusStr)
                        .param("cateringId", cateringId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


    @WithMockUser(authorities = ['CUSTOMER'])
    def "POST api/event/catering returns 200 positive test scenario"() {
        given:
        def customerId = 1L
        def eventId = 2L
        def cateringId = 3L
        def dto = fakeCateringForChosenEventLocationDto

        def catering = fakeFullCateringForChosenEventLocation
        def result = OrganizedEventMapper.toDtoWithCatering(catering.getEventLocation().getEvent())

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(cateringForChosenEventLocationService.create(eq(customerId), eq(eventId), eq(cateringId), eq(dto)))
                .willReturn(catering)

        expect:
        mockMvc.perform(
                post("/api/event/catering")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('customerId', customerId.toString())
                        .param('eventId', eventId.toString())
                        .param('cateringId', cateringId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "DELETE api/event/catering returns 204 positive test scenario"() {
        given:
        def id = 1L

        def cateringForChosenEventLocation = fakeFullCateringForChosenEventLocation
        def result = CateringForChosenLocationMapper.toDto(cateringForChosenEventLocation)

        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(cateringForChosenEventLocationService.cancelReservation(eq(id)))
                .willReturn(cateringForChosenEventLocation)
        expect:
        mockMvc.perform(
                delete("/api/event/catering")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

}
