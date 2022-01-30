package pjatk.socialeventorganizer.social_event_support.appproblem.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.socialeventorganizer.social_event_support.appproblem.mapper.AppProblemMapper
import pjatk.socialeventorganizer.social_event_support.appproblem.model.enums.AppProblemStatusEnum
import pjatk.socialeventorganizer.social_event_support.appproblem.service.AppProblemService
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage
import pjatk.socialeventorganizer.social_event_support.common.tools.CsvTools
import pjatk.socialeventorganizer.social_event_support.enums.AppProblemTypeEnum
import pjatk.socialeventorganizer.social_event_support.test_helper.TestSerializer
import pjatk.socialeventorganizer.social_event_support.trait.problem.AppProblemTrait
import spock.lang.Specification

import java.nio.file.Files
import java.util.stream.Stream

import static org.mockito.ArgumentMatchers.eq
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [AppProblemController.class])
class AppProblemControllerTest extends Specification
        implements AppProblemTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private AppProblemService appProblemService

    @MockBean
    private CsvTools csvTools

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/problems returns 200 positive test scenario"() {
        given:
        def keyword = "sample keyword"
        def pageNo = 1
        def pageSize = 50
        def sort = 'id'
        def order = 'desc'
        def appProblemStatus = AppProblemStatusEnum.ALL

        def customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sort)
                .order(order)
                .build()

        def appProblem = fakeAppProblemWithUser
        def appProblemList = ImmutableList.of(appProblem)
        def resultList = ImmutableList.of(AppProblemMapper.toDtoWithUser(appProblem))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(appProblemService.list(eq(customPage), eq(keyword), eq(appProblemStatus)))
                .willReturn(appProblemList)

        expect:
        mockMvc.perform(
                get("/api/problems")
                        .param("keyword", keyword)
                        .param("pageNo", pageNo.toString())
                        .param("pageSize", pageSize.toString())
                        .param("sort", sort)
                        .param("order", order)
                        .param("status", appProblemStatus.name())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/problems with id param returns 200 positive test scenario"() {
        given:
        def id = 1L

        def appProblem = fakeAppProblemWithUser
        def result = AppProblemMapper.toDtoWithUser(appProblem)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(appProblemService.get(eq(id)))
                .willReturn(appProblem)

        expect:
        mockMvc.perform(
                get("/api/problems")
                        .param("id", id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/problems/export positive test scenario"() {
        given:
        def dateFrom = '01.01.2022'
        def dateTo = '01.02.2022'

        def appProblem = [fakeAppProblemWithUser]
        def result = ImmutableList.of(AppProblemMapper.toDtoWithUser(fakeAppProblemWithUser))
        def path = csvTools.writeToFile(result)

        BDDMockito.given(appProblemService.list(eq(dateFrom), eq(dateTo)))
                .willReturn(appProblem)

        expect:
        mockMvc.perform(
                get("/api/problems/export")
                        .param("dateFrom", dateFrom)
                        .param("dateTo", dateTo)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("text/csv")))
                .andExpect(content().bytes(Files.readAllBytes(new File(path).toPath())))

    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "GET api/problems/types returns 200 positive test scenario"() {
        given:
        def resultList = Stream.of(AppProblemTypeEnum.values())
                .map({ it.getValue() })
                .collect(ImmutableList.toImmutableList())
        def jsonResponse = TestSerializer.serialize(resultList)

        expect:
        mockMvc.perform(
                get("/api/problems/types")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "PUT api/problems with id param returns 200 positive test scenario"() {
        given:
        def id = 1L

        def appProblem = fakeAppProblem
        def result = AppProblemMapper.toDto(appProblem)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(appProblemService.resolve(eq(id)))
                .willReturn(appProblem)

        expect:
        mockMvc.perform(
                put("/api/problems")
                        .param("id", id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }


    @WithMockUser(authorities = ['ADMIN'])
    def "POST api/problems returns 200 positive test scenario"() {
        given:
        def dto = fakeAppProblemDto
        def userId = 1L

        def appProblem = fakeAppProblemWithUser
        def result = AppProblemMapper.toDtoWithUser(appProblem)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(appProblemService.create(eq(dto), eq(userId)))
                .willReturn(appProblem)

        expect:
        mockMvc.perform(
                post("/api/problems")
                        .param("userId", userId.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

}
