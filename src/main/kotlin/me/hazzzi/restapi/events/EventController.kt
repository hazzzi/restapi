package me.hazzzi.restapi.events

import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@Controller
@RequestMapping(value = "/api/events", produces = [MediaTypes.HAL_JSON_UTF8_VALUE])
class EventController constructor(
    val eventRepository: EventRepository
) {
    @PostMapping
    fun createEvent(@RequestBody @Valid eventDto: EventDto, errors: Errors): ResponseEntity<Event> {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build()
        }
        val event = Event(
            id = null,
            name = eventDto.name,
            description = eventDto.description,
            beginEnrollmentDateTime = eventDto.beginEnrollmentDateTime,
            closeEnrollmentDateTime = eventDto.closeEnrollmentDateTime,
            beginEventDateTime = eventDto.beginEventDateTime,
            endEventDateTime = eventDto.endEventDateTime,
            basePrice = eventDto.basePrice,
            maxPrice = eventDto.maxPrice,
            limitOfEnrollment = eventDto.limitOfEnrollment,
            location = eventDto.location,
            free = null,
            offline = null
        )
        val newEvent = eventRepository.save(event)

        // /api/events/10
        val createdUri = linkTo(EventController::class.java).slash(newEvent.id).toUri()

        // response 헤더에 해당 정보를 담고, 바디에 저장한 event 내려줌
        return ResponseEntity.created(createdUri).body(event)
    }
}