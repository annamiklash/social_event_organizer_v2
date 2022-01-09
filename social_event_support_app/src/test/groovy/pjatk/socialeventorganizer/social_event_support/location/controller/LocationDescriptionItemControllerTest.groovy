package pjatk.socialeventorganizer.social_event_support.location.controller

import com.google.common.collect.ImmutableList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.socialeventorganizer.social_event_support.enums.LocationDescriptionItemEnum
import pjatk.socialeventorganizer.social_event_support.location.service.LocationDescriptionItemService
import pjatk.socialeventorganizer.social_event_support.test_helper.TestSerializer
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [LocationDescriptionItemController.class])
class LocationDescriptionItemControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private LocationDescriptionItemService service

    @WithMockUser
    def "GET api/location_description/allowed/all returns 200 positive test scenario"() {
        given:

        def resultList = ImmutableList.copyOf(LocationDescriptionItemEnum.values())
        def jsonResponse = TestSerializer.serialize(resultList)

        expect:
        mockMvc.perform(get('/api/location_description/allowed/all')
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

}
