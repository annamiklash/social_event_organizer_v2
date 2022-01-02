package pjatk.socialeventorganizer.social_event_support.availability.location.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.socialeventorganizer.social_event_support.availability.location.service.LocationAvailabilityService
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper
import pjatk.socialeventorganizer.social_event_support.test_helper.TestSerializer
import pjatk.socialeventorganizer.social_event_support.trait.availability.AvailabilityTrait
import pjatk.socialeventorganizer.social_event_support.trait.availability.LocationAvailabilityTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [LocationAvailabilityController.class])
class LocationAvailabilityControllerTest extends Specification
        implements LocationAvailabilityTrait, AvailabilityTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private LocationAvailabilityService locationAvailabilityService

    @WithMockUser
    def "GET api/availability/location/allowed returns 200 positive test scenario"() {
        given:
        def id = 1L
        def date = "01.01.2021"

        def locationAvailability = fakeLocationAvailability
        def locationAvailabilityList = ImmutableList.of(locationAvailability)
        def resultList = ImmutableList.of(AvailabilityMapper.toDto(locationAvailability))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(locationAvailabilityService.findAllByLocationIdAndDate(eq(id), eq(date)))
                .willReturn(locationAvailabilityList)

        expect:
        mockMvc.perform(
                get("/api/availability/location/allowed")
                        .param("id", id.toString())
                        .param("date", date)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "POST api/availability/location returns 200 positive test scenario"() {
        given:
        def id = 1L

        def locationAvailability = fakeLocationAvailability
        def availabilityDtos = ImmutableList.of(fakeAvailabilityDto)
        def dtos = availabilityDtos.toArray()
        def locationAvailabilityList = ImmutableList.of(locationAvailability)
        def resultList = ImmutableList.of(AvailabilityMapper.toDto(locationAvailability))

        def jsonRequest = TestSerializer.serialize(dtos)
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(locationAvailabilityService.update(eq(availabilityDtos), eq(id), eq(true)))
                .willReturn(locationAvailabilityList)

        expect:
        mockMvc.perform(
                post("/api/availability/location")
                        .param("id", id.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


}
