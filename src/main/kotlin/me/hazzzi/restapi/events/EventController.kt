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
    val eventRepository: EventRepository,
    val eventValidator: EventValidator
) {
    @PostMapping
    fun createEvent(@RequestBody @Valid eventDto: EventDto, errors: Errors): ResponseEntity<Any> {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors)
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

        eventValidator.validate(eventDto, errors)

        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors)
        }
        event.update()
        val newEvent = eventRepository.save(event)
        // /api/events/10
        val selfLinkBuilder = linkTo(EventController::class.java).slash(newEvent.id)
        val createdUri = selfLinkBuilder.toUri()
        // response 헤더에 해당 정보를 담고, 바디에 저장한 event 내려줌
        val eventResource = EventResource(event) // heteoas 적용
        eventResource.add(linkTo(EventController::class.java).withRel("query-events"))
        eventResource.add(selfLinkBuilder.withRel("update-event"))
        return ResponseEntity.created(createdUri).body(eventResource)
    }
}