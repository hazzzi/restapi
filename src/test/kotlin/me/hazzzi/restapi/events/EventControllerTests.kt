package me.hazzzi.restapi.events

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.hateoas.MediaTypes
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@WebMvcTest
class EventControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var eventRepository: EventRepository

    @Test
    fun createEvent() {
        val event = Event(
            id = 10,
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
            free = null,
            offline = null
        )

        Mockito.`when`(eventRepository.save(event)).thenReturn(event)

        mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("id").exists())
    }
}