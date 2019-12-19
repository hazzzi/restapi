package me.hazzzi.restapi.events

import org.springframework.hateoas.Link
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo

class EventResource(event: Event, vararg links: Link?) : Resource<Event?>(event, *links) {
    init {
//        add(Link("http://localhost:8080/api/events/${event.id}"))
        add(linkTo(EventController::class.java).slash(event.id).withSelfRel())
    }
}