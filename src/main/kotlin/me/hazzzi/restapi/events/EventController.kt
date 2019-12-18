package me.hazzzi.restapi.events

import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping(value="/api/events", produces = [MediaTypes.HAL_JSON_UTF8_VALUE])
class EventController constructor(
    val eventRepository: EventRepository
){

    @PostMapping
    fun createEvent(@RequestBody event: Event): ResponseEntity<Event> {
        val newEvent = eventRepository.save(event)
        val createdUri = linkTo(EventController::class.java).slash(newEvent.id).toUri()
        return ResponseEntity.created(createdUri).body(event)
    }
}