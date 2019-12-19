package me.hazzzi.restapi.events

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `정상적으로 이벤트를 생성하는 테스트`() {
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
            .andExpect(status().isCreated)
            .andExpect(jsonPath("id").exists())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
            .andExpect(jsonPath("id").value(Matchers.not(100)))
            .andExpect(jsonPath("free").value(Matchers.not(true)))
            .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT))
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