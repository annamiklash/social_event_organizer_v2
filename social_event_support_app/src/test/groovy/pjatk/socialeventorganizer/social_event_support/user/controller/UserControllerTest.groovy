package pjatk.socialeventorganizer.social_event_support.user.controller

import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.socialeventorganizer.social_event_support.business.mapper.BusinessMapper
import pjatk.socialeventorganizer.social_event_support.business.service.BusinessService
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService
import pjatk.socialeventorganizer.social_event_support.test_helper.TestSerializer
import pjatk.socialeventorganizer.social_event_support.trait.business.BusinessTrait
import pjatk.socialeventorganizer.social_event_support.trait.user.BusinessUserRegistrationDtoTrait
import pjatk.socialeventorganizer.social_event_support.user.service.UserService
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [UserController.class])
class UserControllerTest extends Specification
        implements BusinessTrait,
                BusinessUserRegistrationDtoTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private SecurityService securityService
    @MockBean
    private UserService userService
    @MockBean
    private CustomerService customerService
    @MockBean
    private BusinessService businessService

    @WithMockUser
    def "POST api/register/business returns 200 positive test scenario"() {
        given:
        def dto = fakeBusinessUserRegistrationDto

        def business = fakeVerifiedBusiness
        def result = BusinessMapper.toDtoWithUser(business)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(businessService.createBusinessAccount(eq(dto)))
                .willReturn(business)

        expect:
        mockMvc.perform(
                post("/api/register/business")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }
}
