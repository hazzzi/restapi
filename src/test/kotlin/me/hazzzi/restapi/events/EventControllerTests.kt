package me.hazzzi.restapi.events

import com.fasterxml.jackson.databind.ObjectMapper
import me.hazzzi.restapi.common.RestDocsConfiguration
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.*
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration::class)
class EventControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `정상적으로 이벤트를 생성하는 테스트`() {
        val event = EventDto(
            name = "Spring",
            description = "Rest api with Spring",
            beginEnrollmentDateTime = LocalDateTime.of(2019, 12, 18, 15, 54),
            closeEnrollmentDateTime = LocalDateTime.of(2019, 12, 20, 15, 54),
            beginEventDateTime = LocalDateTime.of(2019, 12, 21, 15, 54),
            endEventDateTime = LocalDateTime.of(2019, 12, 21, 15, 54),
            basePrice = 100,
            maxPrice = 200,
            limitOfEnrollment = 100,
            location = "강남역"
        )

        mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("id").exists())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
            .andExpect(jsonPath("offline").value(true))
            .andExpect(jsonPath("free").value(false))
            .andExpect(jsonPath("eventStatus").exists())
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.query-events").exists())
            .andExpect(jsonPath("_links.update-event").exists())
            .andDo(document("create-event",
                links(
                    linkWithRel("self").description("link to self"),
                    linkWithRel("query-events").description("link to query events"),
                    linkWithRel("update-event").description("link to update an existing event")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                ),
                requestFields(
                    fieldWithPath("name").description("Name of new event"),
                    fieldWithPath("description").description("description of new event"),
                    fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                    fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                    fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                    fieldWithPath("endEventDateTime").description("date time of close of new event"),
                    fieldWithPath("location").description("location of new event"),
                    fieldWithPath("basePrice").description("base price of new event"),
                    fieldWithPath("maxPrice").description("max price of new event"),
                    fieldWithPath("limitOfEnrollment").description("limit of enrollment of new event")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.LOCATION).description("location header"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                ),
                responseFields(
                    fieldWithPath("id").description("id of new event"),
                    fieldWithPath("name").description("Name of new event"),
                    fieldWithPath("description").description("description of new event"),
                    fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                    fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                    fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                    fieldWithPath("endEventDateTime").description("date time of close of new event"),
                    fieldWithPath("location").description("location of new event"),
                    fieldWithPath("basePrice").description("base price of new event"),
                    fieldWithPath("maxPrice").description("max price of new event"),
                    fieldWithPath("limitOfEnrollment").description("limit of enrollment of new event"),
                    fieldWithPath("free").description("it tells if this event is free or not"),
                    fieldWithPath("offline").description("it tells if this event is offline event or not"),
                    fieldWithPath("eventStatus").description("event status"),
                    fieldWithPath("_links.self.href").description("link to self"),
                    fieldWithPath("_links.query-events.href").description("link to query-events"),
                    fieldWithPath("_links.update-event.href").description("link to update-event")
                )
            ))
    }

    @Test
    fun `입력 받을 수 없는 값을 사용한 경우, bad request`() {
        val event = Event(
            id = 100,
            name = "Spring",
            description = "Rest api with Spring",
            beginEnrollmentDateTime = LocalDateTime.of(2019, 12, 18, 15, 54),
            closeEnrollmentDateTime = LocalDateTime.of(2019, 12, 20, 15, 54),
            beginEventDateTime = LocalDateTime.of(2019, 12, 21, 15, 54),
            endEventDateTime = LocalDateTime.of(2019, 12, 21, 15, 54),
            basePrice = 100,
            maxPrice = 200,
            limitOfEnrollment = 100,
            location = "강남역",
            free = true,
            offline = false,
            eventStatus = EventStatus.PUBLISHED
        )

        mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andDo(print())
            .andExpect(status().isBadRequest)

    }

    @Test
    fun `입력 값이 비어있는 경우, bad request`() {
        mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(objectMapper.writeValueAsString("")))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `입력 값이 잘못된 경우, bad request`() {
        val eventDto = EventDto(
            name = "Spring",
            description = "Rest api with Spring",
            beginEnrollmentDateTime = LocalDateTime.of(2019, 12, 18, 15, 54),
            closeEnrollmentDateTime = LocalDateTime.of(2019, 12, 20, 15, 54),
            beginEventDateTime = LocalDateTime.of(2019, 12, 21, 15, 54),
            endEventDateTime = LocalDateTime.of(2019, 12, 20, 15, 54),
            basePrice = 10000,
            maxPrice = 200,
            limitOfEnrollment = 100,
            location = "강남역"
        )
        mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(objectMapper.writeValueAsString(eventDto)))
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andExpect(jsonPath("$[0].objectName").exists())
            .andExpect(jsonPath("$[0].defaultMessage").exists())
            .andExpect(jsonPath("$[0].code").exists())
    }
}