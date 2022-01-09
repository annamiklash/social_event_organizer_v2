package pjatk.socialeventorganizer.social_event_support.cuisine.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.socialeventorganizer.social_event_support.cuisine.mapper.CuisineMapper
import pjatk.socialeventorganizer.social_event_support.cuisine.service.CuisineService
import pjatk.socialeventorganizer.social_event_support.test_helper.TestSerializer
import pjatk.socialeventorganizer.social_event_support.trait.catering.CuisineTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [CuisineController.class])
class CuisineControllerTest extends Specification
        implements CuisineTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private CuisineService cuisineService

    @WithMockUser
    def "GET api/cuisines/allowed/all returns 200 positive test scenario"() {
        given:
        def cuisine = fakeCuisine
        def cuisineList = ImmutableList.of(fakeCuisine)
        def cuisineDto = CuisineMapper.toDto(cuisine)
        def resultList = ImmutableList.of(cuisineDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(cuisineService.list())
                .willReturn(cuisineList)

        expect:
        mockMvc.perform(
                get("/api/cuisines/allowed/all")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "POST /api/cuisines returns 200 positive test scenario"() {
        given:
        def dto = fakeCuisineDto

        def cuisine = fakeCuisine
        def result = CuisineMapper.toDto(cuisine)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(cuisineService.create(eq(dto)))
                .willReturn(cuisine)

        expect:
        mockMvc.perform(
                post("/api/cuisines")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }
}
