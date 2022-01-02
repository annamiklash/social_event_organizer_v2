package pjatk.socialeventorganizer.social_event_support.user.login.controller

import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService
import pjatk.socialeventorganizer.social_event_support.test_helper.TestSerializer
import pjatk.socialeventorganizer.social_event_support.trait.user.UserTrait
import pjatk.socialeventorganizer.social_event_support.user.login.model.request.LoginDto
import pjatk.socialeventorganizer.social_event_support.user.service.UserService
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [LoginController.class])
class LoginControllerTest extends Specification implements UserTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private SecurityService securityService
    @MockBean
    private UserService userService


    def "POST /api/login returns 200 positive test scenario"() {
        given:
        def loginDto = LoginDto.builder()
                .email("test@email.com")
                .password("password")
                .build()


        def jsonRequest = TestSerializer.serialize(loginDto)
        def jsonResponse = TestSerializer.serialize(fakeUserDto)

        BDDMockito.given(userService.isActive(eq(loginDto)))
                .willReturn(true)
        BDDMockito.given(securityService.isPasswordMatch(eq(loginDto)))
                .willReturn(true)
        BDDMockito.given(userService.getUserByEmail(eq(loginDto.getEmail())))
                .willReturn(fakeUser)

        expect:
        mockMvc.perform(
                post("/api/login")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

        BDDMockito.verify(securityService, times(1))
                .buildSecurityContext(eq(loginDto), any())
    }

    def "POST /api/login returns 403 positive test scenario"() {
        given:
        def loginDto = LoginDto.builder()
                .email("test@email.com")
                .password("password")
                .build()


        def jsonRequest = TestSerializer.serialize(loginDto)

        BDDMockito.given(userService.isActive(eq(loginDto)))
                .willReturn(false)

        expect:
        mockMvc.perform(
                post("/api/login")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isForbidden())
    }


    def "POST /api/login returns 401 positive test scenario"() {
        given:
        def loginDto = LoginDto.builder()
                .email("test@email.com")
                .password("password")
                .build()


        def jsonRequest = TestSerializer.serialize(loginDto)

        BDDMockito.given(userService.isActive(eq(loginDto)))
                .willReturn(true)
        BDDMockito.given(securityService.isPasswordMatch(eq(loginDto)))
                .willReturn(false)

        expect:
        mockMvc.perform(
                post("/api/login")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isUnauthorized())
    }


    def "GET /api/logout returns 200 values positive test scenario"() {
        expect:
        mockMvc.perform(
                get("/api/logout")
        )
                .andExpect(status().isOk())

    }
}
